package ru.otus.otuskotlin.mappers.v1

import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId

fun Meme.toTransportCreate(): MemeCreateObject = MemeCreateObject(
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    image = image.takeIf { it.isNotBlank() }
)

fun Meme.toTransportRead(): MemeReadObject = MemeReadObject(
    id = id.takeIf { it != MemeId.NONE }?.value
)

fun Meme.toTransportUpdate(): MemeUpdateObject = MemeUpdateObject(
    id = id.takeIf { it != MemeId.NONE }?.value,
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    image = image.takeIf { it.isNotBlank() }
)

fun Meme.toTransportDelete(): MemeDeleteObject = MemeDeleteObject(
    id = id.takeIf { it != MemeId.NONE }?.value
)

fun List<Meme>.toTransportSearch(): MemeListResponse = MemeListResponse(
    memes = this.map { it.toTransportResponse() }
)

fun Meme.toTransportResponse(): MemeResponse = MemeResponse(
    id = id.takeIf { it != MemeId.NONE }?.value,
    title = title,
    tags = tags,
    imageUrl = image,
    createdAt = createdAt.toString()
)