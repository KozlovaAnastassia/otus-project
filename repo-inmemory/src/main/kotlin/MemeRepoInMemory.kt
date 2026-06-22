package ru.otus.otuskotlin.repo.inmemory

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResult
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.common.NONE

class MemeRepoInMemory(
    private val idGenerator: AtomicLong = AtomicLong(System.currentTimeMillis())
) : IMemeRepo {

    private fun randomId(): String = idGenerator.incrementAndGet().toString()
    private fun randomLock(): String = idGenerator.incrementAndGet().toString()

    private val mutex = Mutex()
    private val storage = ConcurrentHashMap<String, MemeEntity>()

    override suspend fun create(meme: Meme): MemeRepoResult = try {
        val key = if (meme.id != MemeId.NONE) {
            meme.id.asString()
        } else {
            randomId()
        }
        val newMeme = meme.copy(
            id = MemeId(key),
            lock = MemeLock(randomLock()),
            createdAt = if (meme.createdAt != kotlinx.datetime.Instant.NONE) meme.createdAt else Clock.System.now()
        )
        val entity = MemeEntity(newMeme)
        mutex.withLock {
            storage[key] = entity
        }
        MemeRepoResultOk(newMeme)
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "unknown-error",
                message = e.message ?: "Unknown error"
            )
        )
    }

    override suspend fun read(id: MemeId): MemeRepoResult = try {
        val key = id.asString()
        val entity = mutex.withLock {
            storage[key]
        }
        if (entity != null) {
            MemeRepoResultOk(entity.toInternal())
        } else {
            MemeRepoResultErr(
                MemeError(
                    code = "not-found",
                    message = "Meme with id $key not found"
                )
            )
        }
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "unknown-error",
                message = e.message ?: "Unknown error"
            )
        )
    }

    override suspend fun update(meme: Meme): MemeRepoResult = try {
        val key = meme.id.asString()
        val existing = mutex.withLock {
            storage[key]
        }
        if (existing != null) {
            val updated = MemeEntity(meme).copy(id = key)
            mutex.withLock {
                storage[key] = updated
            }
            MemeRepoResultOk(updated.toInternal())
        } else {
            MemeRepoResultErr(
                MemeError(
                    code = "not-found",
                    message = "Meme with id $key not found"
                )
            )
        }
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "unknown-error",
                message = e.message ?: "Unknown error"
            )
        )
    }

    override suspend fun delete(id: MemeId): MemeRepoResult = try {
        val key = id.asString()
        val removed = mutex.withLock {
            storage.remove(key)
        }
        if (removed != null) {
            MemeRepoResultOk(removed.toInternal())
        } else {
            MemeRepoResultErr(
                MemeError(
                    code = "not-found",
                    message = "Meme with id $key not found"
                )
            )
        }
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "unknown-error",
                message = e.message ?: "Unknown error"
            )
        )
    }

    override suspend fun search(filter: MemeFilter): MemeRepoResult = try {
        val result = mutex.withLock {
            storage.values.toList()
        }
            .map { it.toInternal() }
            .filter { meme ->
                val matchTitle = filter.searchString.isEmpty() ||
                        meme.title.contains(filter.searchString, ignoreCase = true)
                val matchTags = filter.tags.isEmpty() ||
                        meme.tags.any { tag -> filter.tags.any { it.equals(tag, ignoreCase = true) } }
                matchTitle && matchTags
            }
        MemeRepoResultOk(result)
    } catch (e: Exception) {
        MemeRepoResultErr(
            MemeError(
                code = "unknown-error",
                message = e.message ?: "Unknown error"
            )
        )
    }

    fun clear() {
        runBlocking {
            mutex.withLock {
                storage.clear()
            }
        }
    }
}