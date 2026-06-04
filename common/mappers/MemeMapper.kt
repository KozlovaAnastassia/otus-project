package ru.otus.otuskotlin.common.mappers

import ru.otus.otuskotlin.api.v1.models.CreateMemeRequest
import ru.otus.otuskotlin.api.v1.models.MemeResponse
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun CreateMemeRequest.toInternal(): Meme = Meme(
    title = this.title ?: "",
    tags = this.tags ?: emptyList(),
    imageUrl = "",
    createdAt = Instant.now(),
    authorId = ""
)

fun Meme.toTransport(): MemeResponse = MemeResponse(
    id = this.id.value.toLongOrNull(),
    title = this.title,
    tags = this.tags,
    imageUrl = this.imageUrl,
    createdAt = this.createdAt.atOffset(ZoneOffset.UTC)
)

fun List<Meme>.toTransportList(): List<MemeResponse> = this.map { it.toTransport() }