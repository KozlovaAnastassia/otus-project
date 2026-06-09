package ru.otus.otuskotlin.common.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import kotlinx.datetime.Instant

class MemeMapperTest {

    @Test
    fun `should map MemeCreateObject to internal Meme`() {
        val request = MemeCreateObject(
            title = "Тестовый мем",
            tags = listOf("кот", "прикол"),
            image = "base64image"
        )

        val meme = request.toInternal()

        assertEquals(request.title, meme.title)
        assertEquals(request.tags, meme.tags)
        assertEquals(MemeId.NONE, meme.id)
        assertEquals("", meme.image)
    }

    @Test
    fun `should map MemeCreateObject with null tags to internal Meme`() {
        val request = MemeCreateObject(
            title = "Мем без тегов",
            tags = null,
            image = null
        )

        val meme = request.toInternal()

        assertEquals(request.title, meme.title)
        assertTrue(meme.tags.isEmpty())
    }

    @Test
    fun `should map MemeUpdateObject to internal Meme`() {
        val request = MemeUpdateObject(
            id = "123",
            title = "Обновлённый мем",
            tags = listOf("обновление", "тест"),
            image = "newBase64image"
        )

        val meme = request.toInternal()

        assertEquals(MemeId("123"), meme.id)
        assertEquals(request.title, meme.title)
        assertEquals(request.tags, meme.tags)
        assertEquals(request.image, meme.image)
    }

    @Test
    fun `should map MemeCreateRequest to context`() {
        val context = MemeContext()
        val request = MemeCreateRequest(
            requestType = "create",
            debug = MemeDebug(
                mode = MemeRequestDebugMode.PROD,
                stub = MemeRequestDebugStubs.SUCCESS
            ),
            meme = MemeCreateObject(
                title = "Контекстный мем",
                tags = listOf("context", "test"),
                image = "base64"
            )
        )

        context.fromTransport(request)

        assertEquals(MemeCommand.CREATE, context.command)
        assertEquals(MemeWorkMode.PROD, context.workMode)
        assertEquals(MemeStubs.SUCCESS, context.stubCase)
        assertNotNull(context.memeRequest)
        assertEquals("Контекстный мем", context.memeRequest?.title)
    }

    @Test
    fun `should map MemeReadRequest to context`() {
        val context = MemeContext()
        val request = MemeReadRequest(
            requestType = "read",
            debug = MemeDebug(mode = MemeRequestDebugMode.TEST),
            meme = MemeReadObject(id = "123")
        )

        context.fromTransport(request)

        assertEquals(MemeCommand.READ, context.command)
        assertEquals(MemeWorkMode.TEST, context.workMode)
        assertEquals(MemeId("123"), context.memeRequest?.id)
    }

    @Test
    fun `should map MemeUpdateRequest to context`() {
        val context = MemeContext()
        val request = MemeUpdateRequest(
            requestType = "update",
            debug = null,
            meme = MemeUpdateObject(
                id = "456",
                title = "Обновляемый мем",
                tags = listOf("update"),
                image = "base64"
            )
        )

        context.fromTransport(request)

        assertEquals(MemeCommand.UPDATE, context.command)
        assertEquals(MemeWorkMode.PROD, context.workMode)
        assertNotNull(context.memeRequest)
        assertEquals(MemeId("456"), context.memeRequest?.id)
        assertEquals("Обновляемый мем", context.memeRequest?.title)
    }

    @Test
    fun `should map MemeDeleteRequest to context`() {
        val context = MemeContext()
        val request = MemeDeleteRequest(
            requestType = "delete",
            debug = null,
            meme = MemeDeleteObject(id = "789")
        )

        context.fromTransport(request)

        assertEquals(MemeCommand.DELETE, context.command)
        assertEquals(MemeWorkMode.PROD, context.workMode)
        assertNotNull(context.memeRequest)
        assertEquals(MemeId("789"), context.memeRequest?.id)
    }

    @Test
    fun `should map MemeSearchRequest to context`() {
        val context = MemeContext()
        val request = MemeSearchRequest(
            requestType = "search",
            debug = MemeDebug(mode = MemeRequestDebugMode.TEST),
            memeFilter = MemeSearchFilter(
                searchString = "кот",
                tags = listOf("смешное", "мем")
            )
        )

        context.fromTransport(request)

        assertEquals(MemeCommand.SEARCH, context.command)
        assertEquals(MemeWorkMode.TEST, context.workMode)
        assertNotNull(context.memeFilterRequest)
        assertEquals("кот", context.memeFilterRequest?.searchString)
        assertEquals(listOf("смешное", "мем"), context.memeFilterRequest?.tags)
    }

    @Test
    fun `should map internal Meme to transport MemeResponseObject`() {
        val now = Instant.NONE
        val meme = Meme(
            id = MemeId("123"),
            title = "Внутренний мем",
            tags = listOf("test", "mapper"),
            image = "/uploads/test.jpg",
            createdAt = now
        )

        val response = meme.toTransportMeme()

        assertEquals("123", response?.id)
        assertEquals(meme.title, response?.title)
        assertEquals(meme.tags, response?.tags)
        assertEquals(meme.image, response?.imageUrl)
    }

    @Test
    fun `should map context to transport MemeCreateResponse`() {
        val context = MemeContext()
        val now = Instant.NONE
        context.memeResponse = Meme(
            id = MemeId("123"),
            title = "Ответный мем",
            tags = listOf("response", "test"),
            image = "/uploads/response.jpg",
            createdAt = now
        )
        context.command = MemeCommand.CREATE
        context.state = MemeState.FINISHING

        val response = context.toTransportMeme() as MemeCreateResponse

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("123", response.meme?.id)
        assertEquals("Ответный мем", response.meme?.title)
        assertEquals(listOf("response", "test"), response.meme?.tags)
        assertEquals("/uploads/response.jpg", response.meme?.imageUrl)
    }

    @Test
    fun `should map context to transport MemeSearchResponse`() {
        val context = MemeContext()
        context.memesResponse = listOf(
            Meme(id = MemeId("1"), title = "Мем 1"),
            Meme(id = MemeId("2"), title = "Мем 2")
        )
        context.command = MemeCommand.SEARCH
        context.state = MemeState.FINISHING

        val response = context.toTransportMeme() as MemeSearchResponse

        assertEquals(2, response.memes?.size)
        assertEquals("Мем 1", response.memes?.get(0)?.title)
        assertEquals("Мем 2", response.memes?.get(1)?.title)
    }

    @Test
    fun `should map list of memes to transport list`() {
        val memes = listOf(
            Meme(id = MemeId("1"), title = "Мем 1"),
            Meme(id = MemeId("2"), title = "Мем 2")
        )

        val responses = memes.toTransportMemeList()

        assertEquals(2, responses?.size)
        assertEquals("1", responses?.get(0)?.id)
        assertEquals("Мем 1", responses?.get(0)?.title)
        assertEquals("2", responses?.get(1)?.id)
        assertEquals("Мем 2", responses?.get(1)?.title)
    }
}