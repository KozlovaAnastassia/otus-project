package ru.otus.otuskotlin.mappers.v1

import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.Command
import ru.otus.otuskotlin.common.models.CommonContext
import ru.otus.otuskotlin.common.models.Error
import ru.otus.otuskotlin.common.models.State
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId

fun CommonContext.toTransportMemeResponse(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.NONE -> throw UnknownCommandException(cmd)
}

fun CommonContext.toTransportCreate(): MemeCreateResponse = MemeCreateResponse(
    responseType = "create",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun CommonContext.toTransportRead(): MemeReadResponse = MemeReadResponse(
    responseType = "read",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun CommonContext.toTransportUpdate(): MemeUpdateResponse = MemeUpdateResponse(
    responseType = "update",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun CommonContext.toTransportDelete(): MemeDeleteResponse = MemeDeleteResponse(
    responseType = "delete",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meme = memeResponse.toTransportMeme()
)

fun CommonContext.toTransportSearch(): MemeSearchResponse = MemeSearchResponse(
    responseType = "search",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    memes = memesResponse.toTransportMemeList()
)

fun Meme?.toTransportMeme(): MemeResponseObject? = this?.let { meme ->
    MemeResponseObject(
        id = meme.id.toTransportId(),
        title = meme.title.takeIf { it.isNotBlank() },
        tags = meme.tags.takeIf { it.isNotEmpty() },
        imageUrl = meme.image.takeIf { it.isNotBlank() },
        createdAt = meme.createdAt.toString()
    )
}

fun List<Meme>.toTransportMemeList(): List<MemeResponseObject>? = this
    .mapNotNull { it.toTransportMeme() }
    .takeIf { it.isNotEmpty() }

private fun MemeId.toTransportId(): String? =
    takeIf { it != MemeId.NONE }?.value

fun List<Error>.toTransportErrors(): List<ru.otus.otuskotlin.api.v1.models.Error>? = this
    .map { it.toTransportError() }
    .takeIf { it.isNotEmpty() }

fun Error.toTransportError(): ru.otus.otuskotlin.api.v1.models.Error =
    ru.otus.otuskotlin.api.v1.models.Error(
        code = this.code.takeIf { it.isNotBlank() },
        group = "request",
        field = this.field.takeIf { it.isNotBlank() },
        message = this.message.takeIf { it.isNotBlank() }
    )

fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FINISHING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.NONE -> null
}

class UnknownCommandException(command: Command) :
    RuntimeException("Unknown command: $command")