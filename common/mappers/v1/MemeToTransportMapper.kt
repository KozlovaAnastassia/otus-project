package ru.otus.otuskotlin.mappers.v1

import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId

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

private fun MemeId.toTransportId(): String? =
    takeIf { it != MemeId.NONE }?.value