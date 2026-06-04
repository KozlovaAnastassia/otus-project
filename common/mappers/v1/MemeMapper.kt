package ru.otus.otuskotlin.mappers.v1

import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.CommonContext
import ru.otus.otuskotlin.common.models.Command
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.models.State
import ru.otus.otuskotlin.common.models.WorkMode
import ru.otus.otuskotlin.common.stubs.Stubs
import ru.otus.otuskotlin.mappers.v1.exceptions.UnknownRequestClass

fun CommonContext.fromTransport(request: IRequest) = when (request) {
    is MemeCreateRequest -> fromTransport(request)
    is MemeReadRequest -> fromTransport(request)
    is MemeUpdateRequest -> fromTransport(request)
    is MemeDeleteRequest -> fromTransport(request)
    is MemeSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toMemeId() = this?.let { MemeId(it) } ?: MemeId.NONE
private fun String?.toMemeIdOrNull() = this?.let { MemeId(it) }

private fun MemeDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    MemeRequestDebugMode.PROD -> WorkMode.PROD
    MemeRequestDebugMode.TEST -> WorkMode.TEST
    MemeRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun MemeDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    MemeRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    MemeRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    MemeRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    MemeRequestDebugStubs.BAD_TITLE -> Stubs.BAD_TITLE
    MemeRequestDebugStubs.BAD_TAGS -> Stubs.BAD_TAGS
    null -> Stubs.NONE
}

fun CommonContext.fromTransport(request: MemeReadRequest) {
    command = Command.READ
    memeId = request.meme?.id.toMemeId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeReadObject?.toInternal(): Meme = if (this != null) {
    Meme(id = id.toMemeId())
} else {
    Meme()
}

fun CommonContext.fromTransport(request: MemeCreateRequest) {
    command = Command.CREATE
    memeRequest = request.meme?.toInternal() ?: Meme()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeCreateObject.toInternal(): Meme = Meme(
    title = this.title ?: "",
    tags = this.tags ?: emptyList(),
    image = this.image ?: ""
)

fun CommonContext.fromTransport(request: MemeUpdateRequest) {
    command = Command.UPDATE
    memeRequest = request.meme?.toInternal() ?: Meme()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeUpdateObject.toInternal(): Meme = Meme(
    id = this.id.toMemeId(),
    title = this.title ?: "",
    tags = this.tags ?: emptyList(),
    image = this.image ?: ""
)

fun CommonContext.fromTransport(request: MemeDeleteRequest) {
    command = Command.DELETE
    memeRequest = request.meme.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeDeleteObject?.toInternal(): Meme = if (this != null) {
    Meme(
        id = id.toMemeId(),
    )
} else {
    Meme()
}

fun CommonContext.fromTransport(request: MemeSearchRequest) {
    command = Command.SEARCH
    memeFilterRequest = request.memeFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeSearchFilter?.toInternal(): MemeFilter = MemeFilter(
    searchString = this?.searchString ?: "",
    tags = this?.tags ?: emptyList()
)

fun CommonContext.toTransportMemeResponse(): MemeResponse {
    val meme = memeResponse ?: Meme()
    return MemeResponse(
        id = meme.id.value,
        title = meme.title,
        tags = meme.tags,
        imageUrl = meme.imageUrl,
        createdAt = meme.createdAt.toString()
    )
}

fun CommonContext.toTransportMemeListResponse(): MemeListResponse {
    return MemeListResponse(
        memes = memesResponse.map { meme ->
            MemeResponse(
                id = meme.id.value,
                title = meme.title,
                tags = meme.tags,
                imageUrl = meme.imageUrl,
                createdAt = meme.createdAt.toString()
            )
        }
    )
}

fun Meme.toTransport(): MemeResponse = MemeResponse(
    id = this.id.value,
    title = this.title,
    tags = this.tags,
    imageUrl = this.image,
    createdAt = this.createdAt.toString()
)

fun List<Meme>.toTransportList(): List<MemeResponse> = this.map { it.toTransport() }