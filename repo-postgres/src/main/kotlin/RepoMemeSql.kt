package ru.otus.otuskotlin.repo.postgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResult
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk
import java.sql.Connection
import java.util.concurrent.atomic.AtomicLong

class RepoMemeSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { (AtomicLong(System.currentTimeMillis()).incrementAndGet()).toString() }
) : IMemeRepo {

    private val memeTable = MemeTable("${properties.schema}.${properties.table}")

    private val conn = Database.connect(
        properties.url,
        driver = "org.postgresql.Driver",
        user = properties.user,
        password = properties.password
    )

    fun clear(): Unit = transaction(conn) {
        memeTable.deleteAll()
    }

    private fun saveObj(meme: Meme): Meme = transaction(conn) {
        val id = meme.id.takeIf { it != MemeId.NONE }?.asString() ?: randomUuid()
        val newMeme = meme.copy(
            id = MemeId(id),
            lock = meme.lock.takeIf { it != MemeLock.NONE } ?: MemeLock(randomUuid())
        )

        memeTable.insert {
            it[memeTable.id] = id
            it[memeTable.title] = newMeme.title.takeIf { it.isNotBlank() }
            it[memeTable.tags] = newMeme.tags.joinToString(",")
            it[memeTable.image] = newMeme.image.takeIf { it.isNotBlank() }
            it[memeTable.imageUrl] = newMeme.imageUrl.takeIf { it.isNotBlank() }
            it[memeTable.createdAt] = newMeme.createdAt.toString()
            it[memeTable.authorId] = newMeme.authorId.asString().takeIf { it.isNotBlank() }
            it[memeTable.lock] = newMeme.lock.asString()
            it[memeTable.visibility] = newMeme.visibility
        }
        newMeme
    }

    fun createTable() = transaction(conn) {
        SchemaUtils.create(memeTable)
    }

    // CREATE
    override suspend fun create(meme: Meme): MemeRepoResult = try {
        val saved = withContext(Dispatchers.IO) {
            transaction(conn) {
                saveObj(meme)
            }
        }
        MemeRepoResultOk(saved)
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "db-error",
                message = e.message ?: "Unknown database error"
            )
        )
    }

    // READ
    override suspend fun read(id: MemeId): MemeRepoResult = try {
        val result = withContext(Dispatchers.IO) {
            transaction(conn) {
                memeTable.select { memeTable.id eq id.asString() }
                    .singleOrNull()
                    ?.let { memeTable.from(it) }
            }
        }

        if (result != null) {
            MemeRepoResultOk(result)
        } else {
            MemeRepoResultErr(
                MemeError(
                    code = "not-found",
                    message = "Meme with id ${id.asString()} not found"
                )
            )
        }
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "db-error",
                message = e.message ?: "Unknown database error"
            )
        )
    }

    // UPDATE
    override suspend fun update(meme: Meme): MemeRepoResult = try {
        val id = meme.id.asString()

        val updated = withContext(Dispatchers.IO) {
            transaction(conn) {
                val existing = memeTable.select { memeTable.id eq id }
                    .singleOrNull()

                if (existing != null) {
                    memeTable.update({ memeTable.id eq id }) {
                        it[memeTable.title] = meme.title.takeIf { it.isNotBlank() }
                        it[memeTable.tags] = meme.tags.joinToString(",")
                        it[memeTable.image] = meme.image.takeIf { it.isNotBlank() }
                        it[memeTable.imageUrl] = meme.imageUrl.takeIf { it.isNotBlank() }
                        it[memeTable.authorId] = meme.authorId.asString().takeIf { it.isNotBlank() }
                        it[memeTable.lock] = meme.lock.takeIf { it != MemeLock.NONE }?.asString() ?: randomUuid()
                        it[memeTable.visibility] = meme.visibility
                    }

                    memeTable.select { memeTable.id eq id }
                        .singleOrNull()
                        ?.let { memeTable.from(it) }
                } else {
                    null
                }
            }
        }

        if (updated != null) {
            MemeRepoResultOk(updated)
        } else {
            MemeRepoResultErr(
                MemeError(
                    code = "not-found",
                    message = "Meme with id $id not found"
                )
            )
        }
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "db-error",
                message = e.message ?: "Unknown database error"
            )
        )
    }

    // DELETE
    override suspend fun delete(id: MemeId): MemeRepoResult = try {
        val deleted = withContext(Dispatchers.IO) {
            transaction(conn) {
                memeTable.deleteWhere { memeTable.id eq id.asString() }
            }
        }

        if (deleted > 0) {
            MemeRepoResultOk(emptyList())
        } else {
            MemeRepoResultErr(
                MemeError(
                    code = "not-found",
                    message = "Meme with id ${id.asString()} not found"
                )
            )
        }
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "db-error",
                message = e.message ?: "Unknown database error"
            )
        )
    }

    // SEARCH
    override suspend fun search(filter: MemeFilter): MemeRepoResult = try {
        val results = withContext(Dispatchers.IO) {
            transaction(conn) {
                val conditions = mutableListOf<Op<Boolean>>()

                if (filter.searchString.isNotBlank()) {
                    conditions.add(memeTable.title like "%${filter.searchString}%")
                }

                if (filter.tags.isNotEmpty()) {
                    filter.tags.forEach { tag ->
                        conditions.add(memeTable.tags like "%$tag%")
                    }
                }

                if (conditions.isNotEmpty()) {
                    val whereCondition = conditions.reduce { acc, op -> acc and op }
                    memeTable.select { whereCondition }
                        .map { memeTable.from(it) }
                } else {
                    memeTable.selectAll()
                        .map { memeTable.from(it) }
                }
            }
        }

        MemeRepoResultOk(results)
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "db-error",
                message = e.message ?: "Unknown database error"
            )
        )
    }

    fun getConnection(): Connection {
        return conn.connector() as Connection
    }
}