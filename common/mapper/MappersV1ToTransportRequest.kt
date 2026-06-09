package ru.otus.otuskotlin.mappers.v1

import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.models.MemeLock
import ru.otus.otuskotlin.common.models.MemeVisibility

fun Meme.toTransportCreateMeme(): MemeCreateObject = MemeCreateObject(
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    image = image.takeIf { it.isNotBlank() },
    visibility = visibility.toTransportVisibility()
)

fun Meme.toTransportReadMeme(): MemeReadObject = MemeReadObject(
    id = id.toTransportId()
)

fun Meme.toTransportUpdateMeme(): MemeUpdateObject = MemeUpdateObject(
    id = id.toTransportId(),
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    image = image.takeIf { it.isNotBlank() },
    visibility = visibility.toTransportVisibility(),
    lock = lock.toTransportLock()
)

fun Meme.toTransportDeleteMeme(): MemeDeleteObject = MemeDeleteObject(
    id = id.toTransportId()
)

private fun MemeId.toTransportId(): String? = takeIf { it != MemeId.NONE }?.asString()

private fun MemeLock.toTransportLock(): String? = takeIf { it != MemeLock.NONE }?.asString()

private fun MemeVisibility.toTransportVisibility(): MemeVisibility? = when (this) {
    MemeVisibility.PUBLIC -> MemeVisibility.PUBLIC
    MemeVisibility.REGISTERED_ONLY -> MemeVisibility.REGISTERED_ONLY
    MemeVisibility.OWNER_ONLY -> MemeVisibility.OWNER_ONLY
    MemeVisibility.NONE -> null
}