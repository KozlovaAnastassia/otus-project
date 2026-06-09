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
    MemeRequestDebugMode.PROD -> MemeWorkMode.PROD
    MemeRequestDebugMode.TEST -> MemeWorkMode.TEST
    MemeRequestDebugMode.STUB -> MemeWorkMode.STUB
    null -> MemeWorkMode.PROD
}

private fun MemeDebug?.transportToStubCase(): MemeStubs = when (this?.stub) {
    MemeRequestDebugStubs.SUCCESS -> MemeStubs.SUCCESS
    MemeRequestDebugStubs.NOT_FOUND -> MemeStubs.NOT_FOUND
    MemeRequestDebugStubs.BAD_ID -> MemeStubs.BAD_ID
    MemeRequestDebugStubs.BAD_TITLE -> MemeStubs.BAD_TITLE
    MemeRequestDebugStubs.BAD_TAGS -> MemeStubs.BAD_TAGS
    MemeRequestDebugStubs.BAD_IMAGE -> MemeStubs.BAD_IMAGE
    MemeRequestDebugStubs.CANNOT_DELETE -> MemeStubs.CANNOT_DELETE
    MemeRequestDebugStubs.BAD_SEARCH_STRING -> MemeStubs.BAD_SEARCH_STRING
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
    image = this.image ?: "",
    visibility = this.visibility.fromTransport()
)

private fun MemeUpdateObject.toInternal(): Meme = Meme(
    id = this.id.toMemeId(),
    title = this.title ?: "",
    tags = this.tags ?: emptyList(),
    image = this.image ?: "",
    visibility = this.visibility.fromTransport(),
    lock = this.lock.toMemeLock()
)

private fun MemeVisibility?.fromTransport(): MemeVisibility = when (this) {
    MemeVisibility.VISIBLE_TO_OWNER -> MemeVisibility.VISIBLE_TO_OWNER
    MemeVisibility.VISIBLE_TO_GROUP -> MemeVisibility.VISIBLE_TO_GROUP
    MemeVisibility.VISIBLE_PUBLIC -> MemeVisibility.VISIBLE_PUBLIC
    null -> MemeVisibility.NONE
}