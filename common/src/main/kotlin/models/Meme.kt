package ru.otus.otuskotlin.common.models

import MemePermissionClient
import MemeVisibility
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.common.NONE

data class Meme(
    var id: MemeId = MemeId.NONE,
    var title: String = "",
    var tags: List<String> = emptyList(),
    var image: String = "",
    var imageUrl: String = "",
    var createdAt: Instant = Instant.NONE,
    var lock: MemeLock = MemeLock.NONE,
    var authorId: MemeUserId = MemeUserId.NONE,
    val permissionsClient: MutableSet<MemePermissionClient> = mutableSetOf(),
    var visibility: MemeVisibility = MemeVisibility.NONE,
) {
    fun deepCopy(): Meme = copy(
        tags = tags.toList(),
    )

    fun isEmpty(): Boolean = this == NONE

    companion object {
        val NONE = Meme()
    }
}