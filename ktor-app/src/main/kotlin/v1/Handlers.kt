package ru.otus.otuskotlin.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.mappers.v1.fromTransport
import ru.otus.otuskotlin.mappers.v1.toTransportMeme
import ru.otus.otuskotlin.app.ktor.config.MemeAppSettings
import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.models.MemeCommand
import ru.otus.otuskotlin.common.models.MemeWorkMode

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    command: MemeCommand,
    appSettings: MemeAppSettings,
    stubCase: MemeStubs = MemeStubs.SUCCESS
) {
    val request = receive<Q>()

    val context = MemeContext().apply {
        this.command = command
        workMode = MemeWorkMode.STUB
        this.stubCase = stubCase
        fromTransport(request)
    }

    val processor = MemeProcessor(appSettings.repo)
    processor.exec(context)

    respond(context.toTransportMeme())
}

suspend fun ApplicationCall.createMeme(appSettings: MemeAppSettings) {
    processV1<MemeCreateRequest, MemeCreateResponse>(MemeCommand.CREATE, appSettings, MemeStubs.SUCCESS)
}

suspend fun ApplicationCall.readMeme(appSettings: MemeAppSettings) {
    processV1<MemeReadRequest, MemeReadResponse>(MemeCommand.READ, appSettings, MemeStubs.SUCCESS)
}

suspend fun ApplicationCall.updateMeme(appSettings: MemeAppSettings) {
    processV1<MemeUpdateRequest, MemeUpdateResponse>(MemeCommand.UPDATE, appSettings, MemeStubs.SUCCESS)
}

suspend fun ApplicationCall.deleteMeme(appSettings: MemeAppSettings) {
    processV1<MemeDeleteRequest, MemeDeleteResponse>(MemeCommand.DELETE, appSettings, MemeStubs.SUCCESS)
}

suspend fun ApplicationCall.searchMeme(appSettings: MemeAppSettings) {
    processV1<MemeSearchRequest, MemeSearchResponse>(MemeCommand.SEARCH, appSettings, MemeStubs.SUCCESS)
}