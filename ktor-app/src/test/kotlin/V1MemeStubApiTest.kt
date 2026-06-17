package ru.otus.otuskotlin.app.ktor

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeCommand
import ru.otus.otuskotlin.common.models.MemeWorkMode
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.mappers.v1.fromTransport
import ru.otus.otuskotlin.mappers.v1.toTransportMeme

class MappersTest {

    @Test
    fun testCreateMapping() {
        val context = MemeContext().apply {
            command = MemeCommand.CREATE
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.SUCCESS

            val request = MemeCreateRequest(
                requestType = "create",
                meme = MemeCreateObject(
                    title = "Тестовый мем",
                    tags = listOf("кот", "смешное"),
                    image = "base64image"
                )
            )
            fromTransport(request)

            memeResponse = ru.otus.otuskotlin.common.models.Meme(
                id = ru.otus.otuskotlin.common.models.MemeId("123"),
                title = memeRequest?.title ?: "",
                tags = memeRequest?.tags ?: emptyList(),
                image = memeRequest?.image ?: ""
            )
        }

        val response = context.toTransportMeme() as MemeCreateResponse
        assertEquals("123", response.meme?.id)
        assertEquals("Тестовый мем", response.meme?.title)
    }

    @Test
    fun testReadMapping() {
        val context = MemeContext().apply {
            command = MemeCommand.READ
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.SUCCESS

            val request = MemeReadRequest(
                requestType = "read",
                meme = MemeReadObject(id = "456")
            )
            fromTransport(request)

            memeResponse = ru.otus.otuskotlin.common.models.Meme(
                id = ru.otus.otuskotlin.common.models.MemeId("456"),
                title = "Мем 456",
                tags = listOf("тест")
            )
        }

        val response = context.toTransportMeme() as MemeReadResponse
        assertEquals("456", response.meme?.id)
    }

    @Test
    fun testSearchMapping() {
        val context = MemeContext().apply {
            command = MemeCommand.SEARCH
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.SUCCESS

            val request = MemeSearchRequest(
                requestType = "search",
                memeFilter = MemeSearchFilter(searchString = "кот")
            )
            fromTransport(request)

            memesResponse = mutableListOf(
                ru.otus.otuskotlin.common.models.Meme(
                    id = ru.otus.otuskotlin.common.models.MemeId("1"),
                    title = "Первый мем"
                ),
                ru.otus.otuskotlin.common.models.Meme(
                    id = ru.otus.otuskotlin.common.models.MemeId("2"),
                    title = "Второй мем"
                )
            )
        }

        val response = context.toTransportMeme() as MemeSearchResponse
        assertEquals(2, response.memes?.size)
    }
}