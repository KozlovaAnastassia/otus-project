package ru.otus.otuskotlin.mappers.v1

import MemeVisibility
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.models.MemeLock

fun Meme.toTransportCreateMeme(): MemeCreateObject = MemeCreateObject(
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    image = image.takeIf { it.isNotBlank() }
)

fun Meme.toTransportReadMeme(): MemeReadObject = MemeReadObject(
    id = id.toTransportId()
)

fun Meme.toTransportUpdateMeme(): MemeUpdateObject = MemeUpdateObject(
    id = id.toTransportId(),
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    image = image.takeIf { it.isNotBlank() }
)

fun Meme.toTransportDeleteMeme(): MemeDeleteObject = MemeDeleteObject(
    id = id.toTransportId()
)

private fun MemeId.toTransportId(): String? = takeIf { it != MemeId.NONE }?.asString()

private fun MemeLock.toTransportLock(): String? = takeIf { it != MemeLock.NONE }?.asString()

private fun MemeVisibility.toTransportVisibility(): MemeVisibility? = when (this) {
    MemeVisibility.VISIBLE_PUBLIC -> MemeVisibility.VISIBLE_PUBLIC
    MemeVisibility.VISIBLE_TO_GROUP -> MemeVisibility.VISIBLE_TO_GROUP
    MemeVisibility.VISIBLE_TO_OWNER -> MemeVisibility.VISIBLE_TO_OWNER
    MemeVisibility.NONE -> null
}