package ru.otus.otuskotlin.mappers.v1

import MemePermissionClient
import MemeVisibility
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.exceptions.UnknownMemeCommand
import ru.otus.otuskotlin.common.models.*

fun MemeContext.toTransportMeme(): IResponse = when (val cmd = command) {
    MemeCommand.CREATE -> toTransportCreate()
    MemeCommand.READ -> toTransportRead()
    MemeCommand.UPDATE -> toTransportUpdate()
    MemeCommand.DELETE -> toTransportDelete()
    MemeCommand.SEARCH -> toTransportSearch()
    MemeCommand.NONE -> throw UnknownMemeCommand(cmd)
}

fun MemeContext.toTransportCreate(): MemeCreateResponse = MemeCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun MemeContext.toTransportRead(): MemeReadResponse = MemeReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun MemeContext.toTransportUpdate(): MemeUpdateResponse = MemeUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun MemeContext.toTransportDelete(): MemeDeleteResponse = MemeDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun MemeContext.toTransportSearch(): MemeSearchResponse = MemeSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    memes = memesResponse.toTransportMemeList()
)

fun List<Meme>.toTransportMemeList(): List<MemeResponseObject>? = this
    .mapNotNull { it.toTransportMeme() }
    .takeIf { it.isNotEmpty() }

fun Meme.toTransportMeme(): MemeResponseObject? = MemeResponseObject(
    id = id.takeIf { it != MemeId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    tags = tags.takeIf { it.isNotEmpty() },
    imageUrl = (imageUrl.takeIf { it.isNotBlank() } ?: image).takeIf { it.isNotBlank() }
).takeIf { !this@toTransportMeme.isEmpty() }

private fun MemeId.toTransportId(): String? = takeIf { it != MemeId.NONE }?.asString()

private fun MemeVisibility.toTransportVisibility(): MemeVisibility? = when (this) {
    MemeVisibility.VISIBLE_TO_OWNER -> MemeVisibility.VISIBLE_TO_OWNER
    MemeVisibility.VISIBLE_TO_GROUP -> MemeVisibility.VISIBLE_TO_GROUP
    MemeVisibility.VISIBLE_PUBLIC -> MemeVisibility.VISIBLE_PUBLIC
    MemeVisibility.NONE -> null
}

private fun List<MemeError>.toTransportErrors(): List<ru.otus.otuskotlin.api.v1.models.Error>? = this
    .map { it.toTransportError() }
    .takeIf { it.isNotEmpty() }

private fun MemeError.toTransportError(): ru.otus.otuskotlin.api.v1.models.Error = ru.otus.otuskotlin.api.v1.models.Error(
    code = code.takeIf { it.isNotBlank() },
    group = "request",
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun MemeState.toResult(): ResponseResult? = when (this) {
    MemeState.RUNNING -> ResponseResult.success
    MemeState.FINISHING -> ResponseResult.success
    MemeState.FAILING -> ResponseResult.error
    MemeState.NONE -> null
}