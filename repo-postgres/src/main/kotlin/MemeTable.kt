package ru.otus.otuskotlin.repo.postgres

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.common.NONE
import ru.otus.otuskotlin.common.models.*

class MemeTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val tags = text(SqlFields.TAGS).nullable()
    val image = text(SqlFields.IMAGE).nullable()
    val imageUrl = text(SqlFields.IMAGE_URL).nullable()
    val createdAt = text(SqlFields.CREATED_AT).nullable()
    val authorId = text(SqlFields.AUTHOR_ID).nullable()
    val lock = text(SqlFields.LOCK)
    val visibility = text(SqlFields.VISIBILITY).nullable()  // ← изменили на text

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow): Meme = Meme(
        id = MemeId(res[id]),
        title = res[title] ?: "",
        tags = res[tags]?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
        image = res[image] ?: "",
        imageUrl = res[imageUrl] ?: "",
        createdAt = res[createdAt]?.let { kotlinx.datetime.Instant.parse(it) } ?: kotlinx.datetime.Instant.NONE,
        authorId = res[authorId]?.let { MemeUserId(it) } ?: MemeUserId.NONE,
        lock = MemeLock(res[lock]),
        visibility = res[visibility]?.let { MemeVisibility.valueOf(it) } ?: MemeVisibility.NONE
    )

    fun UpdateBuilder<*>.to(meme: Meme, randomUuid: () -> String) {
        this[id] = meme.id.takeIf { it != MemeId.NONE }?.asString() ?: randomUuid()
        this[title] = meme.title
        this[tags] = meme.tags.joinToString(",")
        this[image] = meme.image
        this[imageUrl] = meme.imageUrl
        this[createdAt] = meme.createdAt.toString()
        this[authorId] = meme.authorId.asString()
        this[lock] = meme.lock.takeIf { it != MemeLock.NONE }?.asString() ?: randomUuid()
        this[visibility] = meme.visibility.name  // ← сохраняем как строку
    }
}