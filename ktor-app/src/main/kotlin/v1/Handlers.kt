package ru.otus.otuskotlin.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.mappers.v1.fromTransport
import ru.otus.otuskotlin.mappers.v1.toTransportMeme
import kotlinx.datetime.Clock

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    command: MemeCommand,
    stubCase: MemeStubs = MemeStubs.SUCCESS
) {
    val request = receive<Q>()

    val context = MemeContext().apply {
        this.command = command
        workMode = MemeWorkMode.STUB
        this.stubCase = stubCase
        fromTransport(request)
    }

    when (command) {
        MemeCommand.CREATE -> {
            context.memeResponse = Meme(
                id = MemeId("123"),
                title = context.memeRequest?.title ?: "Тестовый мем",
                tags = context.memeRequest?.tags ?: emptyList(),
                image = context.memeRequest?.image ?: "",
                createdAt = Clock.System.now()
            )
        }
        MemeCommand.READ -> {
            context.memeResponse = Meme(
                id = context.memeRequest.id,
                title = "Мем ${context.memeRequest.id}",
                tags = listOf("тест"),
                image = "/uploads/test.jpg",
                createdAt = Clock.System.now()
            )
        }
        MemeCommand.UPDATE -> {
            context.memeResponse = context.memeRequest?.copy(
                title = context.memeRequest?.title ?: "Обновлённый мем"
            ) ?: Meme()
        }
        MemeCommand.DELETE -> {
            context.memeResponse = context.memeRequest
        }
        MemeCommand.SEARCH -> {
            context.memesResponse = mutableListOf(
                Meme(id = MemeId("1"), title = "Первый мем", tags = listOf("кот"), image = "/uploads/1.jpg"),
                Meme(id = MemeId("2"), title = "Второй мем", tags = listOf("собака"), image = "/uploads/2.jpg")
            )
        }
        else -> {}
    }

    context.state = MemeState.FINISHING

    respond(context.toTransportMeme())
}

suspend fun ApplicationCall.createMeme() {
    processV1<MemeCreateRequest, MemeCreateResponse>(MemeCommand.CREATE)
}

suspend fun ApplicationCall.readMeme() {
    processV1<MemeReadRequest, MemeReadResponse>(MemeCommand.READ)
}

suspend fun ApplicationCall.updateMeme() {
    processV1<MemeUpdateRequest, MemeUpdateResponse>(MemeCommand.UPDATE)
}

suspend fun ApplicationCall.deleteMeme() {
    processV1<MemeDeleteRequest, MemeDeleteResponse>(MemeCommand.DELETE)
}

suspend fun ApplicationCall.searchMeme() {
    processV1<MemeSearchRequest, MemeSearchResponse>(MemeCommand.SEARCH)
}