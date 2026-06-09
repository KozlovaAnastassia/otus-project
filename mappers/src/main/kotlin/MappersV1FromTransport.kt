package ru.otus.otuskotlin.mappers.v1

import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.mappers.v1.exceptions.UnknownRequestClass

fun MemeContext.fromTransport(request: IRequest) = when (request) {
    is MemeCreateRequest -> fromTransport(request)
    is MemeReadRequest -> fromTransport(request)
    is MemeUpdateRequest -> fromTransport(request)
    is MemeDeleteRequest -> fromTransport(request)
    is MemeSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toMemeId() = this?.let { MemeId(it) } ?: MemeId.NONE
private fun String?.toMemeUserId() = this?.let { MemeUserId(it) } ?: MemeUserId.NONE
private fun String?.toMemeLock() = this?.let { MemeLock(it) } ?: MemeLock.NONE

private fun MemeDebug?.transportToWorkMode(): MemeWorkMode = when (this?.mode) {
    MemeRequestDebugMode.prod -> MemeWorkMode.PROD
    MemeRequestDebugMode.test -> MemeWorkMode.TEST
    MemeRequestDebugMode.stub -> MemeWorkMode.STUB
    null -> MemeWorkMode.PROD
}

private fun MemeDebug?.transportToStubCase(): MemeStubs = when (this?.stub) {
    MemeRequestDebugStubs.success -> MemeStubs.SUCCESS
    MemeRequestDebugStubs.notFound -> MemeStubs.NOT_FOUND
    MemeRequestDebugStubs.badId -> MemeStubs.BAD_ID
    MemeRequestDebugStubs.badTitle -> MemeStubs.BAD_TITLE
    MemeRequestDebugStubs.badTags -> MemeStubs.BAD_TAGS
    MemeRequestDebugStubs.badImage -> MemeStubs.BAD_IMAGE
    MemeRequestDebugStubs.cannotDelete -> MemeStubs.CANNOT_DELETE
    MemeRequestDebugStubs.badSearchString -> MemeStubs.BAD_SEARCH_STRING
    null -> MemeStubs.NONE
}

fun MemeContext.fromTransport(request: MemeReadRequest) {
    command = MemeCommand.READ
    memeRequest = request.meme.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeReadObject?.toInternal(): Meme = if (this != null) {
    Meme(id = id.toMemeId())
} else {
    Meme()
}

fun MemeContext.fromTransport(request: MemeCreateRequest) {
    command = MemeCommand.CREATE
    memeRequest = request.meme?.toInternal() ?: Meme()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MemeContext.fromTransport(request: MemeUpdateRequest) {
    command = MemeCommand.UPDATE
    memeRequest = request.meme?.toInternal() ?: Meme()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MemeContext.fromTransport(request: MemeDeleteRequest) {
    command = MemeCommand.DELETE
    memeRequest = request.meme.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeDeleteObject?.toInternal(): Meme = if (this != null) {
    Meme(id = id.toMemeId())
} else {
    Meme()
}

fun MemeContext.fromTransport(request: MemeSearchRequest) {
    command = MemeCommand.SEARCH
    memeFilterRequest = request.memeFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MemeSearchFilter?.toInternal(): MemeFilter = MemeFilter(
    searchString = this?.searchString ?: "",
    tags = this?.tags ?: emptyList()
)

private fun MemeCreateObject.toInternal(): Meme = Meme(
    title = this.title ?: "",
    tags = this.tags ?: emptyList(),
    image = this.image ?: ""
)

private fun MemeUpdateObject.toInternal(): Meme = Meme(
    id = this.id.toMemeId(),
    title = this.title ?: "",
    tags = this.tags ?: emptyList(),
    image = this.image ?: ""
)