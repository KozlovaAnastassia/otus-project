package ru.otus.otuskotlin.repo.inmemory

import ru.otus.otuskotlin.common.models.*
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.common.NONE

data class MemeEntity(
    val id: String? = null,
    val title: String? = null,
    val tags: List<String>? = null,
    val image: String? = null,
    val imageUrl: String? = null,
    val createdAt: Instant? = null,
    val authorId: String? = null,
    val lock: String? = null,
    val visibility: String? = null
) {
    constructor(model: Meme) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        tags = model.tags.takeIf { it.isNotEmpty() },
        image = model.image.takeIf { it.isNotBlank() },
        imageUrl = model.imageUrl.takeIf { it.isNotBlank() },
        createdAt = model.createdAt.takeIf { it != Instant.NONE },
        authorId = model.authorId.asString().takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != MemeVisibility.NONE }?.name
    )

    fun toInternal(): Meme = Meme(
        id = id?.let { MemeId(it) } ?: MemeId.NONE,
        title = title ?: "",
        tags = tags ?: emptyList(),
        image = image ?: "",
        imageUrl = imageUrl ?: "",
        createdAt = createdAt ?: Instant.NONE,
        authorId = authorId?.let { MemeUserId(it) } ?: MemeUserId.NONE,
        lock = lock?.let { MemeLock(it) } ?: MemeLock.NONE,
        visibility = visibility?.let { MemeVisibility.valueOf(it) } ?: MemeVisibility.NONE
    )
}