package ru.otus.otuskotlin.common.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.models.*
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.Stubs
import java.time.Instant

class MemeMapperTest {

    @Test
    fun `should map CreateMemeRequest to internal Meme`() {
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
    fun `should map CreateMemeRequest with null tags to internal Meme`() {
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
    fun `should map UpdateMemeRequest to internal Meme`() {
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
        val context = CommonContext()
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

        assertEquals(Command.CREATE, context.command)
        assertEquals(WorkMode.PROD, context.workMode)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertNotNull(context.memeRequest)
        assertEquals("Контекстный мем", context.memeRequest?.title)
    }

    @Test
    fun `should map MemeReadRequest to context`() {
        val context = CommonContext()
        val request = MemeReadRequest(
            requestType = "read",
            debug = MemeDebug(mode = MemeRequestDebugMode.TEST),
            meme = MemeReadObject(id = "123")
        )

        context.fromTransport(request)

        assertEquals(Command.READ, context.command)
        assertEquals(WorkMode.TEST, context.workMode)
        assertEquals(MemeId("123"), context.memeId)
    }

    @Test
    fun `should map MemeUpdateRequest to context`() {
        val context = CommonContext()
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

        assertEquals(Command.UPDATE, context.command)
        assertEquals(WorkMode.PROD, context.workMode)
        assertNotNull(context.memeRequest)
        assertEquals(MemeId("456"), context.memeRequest?.id)
        assertEquals("Обновляемый мем", context.memeRequest?.title)
    }

    @Test
    fun `should map MemeDeleteRequest to context`() {
        val context = CommonContext()
        val request = MemeDeleteRequest(
            requestType = "delete",
            debug = null,
            meme = MemeDeleteObject(id = "789")
        )

        context.fromTransport(request)

        assertEquals(Command.DELETE, context.command)
        assertEquals(WorkMode.PROD, context.workMode)
        assertNotNull(context.memeRequest)
        assertEquals(MemeId("789"), context.memeRequest?.id)
    }

    @Test
    fun `should map MemeSearchRequest to context`() {
        val context = CommonContext()
        val request = MemeSearchRequest(
            requestType = "search",
            debug = MemeDebug(mode = MemeRequestDebugMode.TEST),
            memeFilter = MemeSearchFilter(
                searchString = "кот",
                tags = listOf("смешное", "мем")
            )
        )

        context.fromTransport(request)

        assertEquals(Command.SEARCH, context.command)
        assertEquals(WorkMode.TEST, context.workMode)
        assertNotNull(context.memeFilterRequest)
        assertEquals("кот", context.memeFilterRequest?.searchString)
        assertEquals(listOf("смешное", "мем"), context.memeFilterRequest?.tags)
    }

    @Test
    fun `should map internal Meme to transport MemeResponse`() {
        val now = Instant.now()
        val meme = Meme(
            id = MemeId("123"),
            title = "Внутренний мем",
            tags = listOf("test", "mapper"),
            image = "/uploads/test.jpg",
            createdAt = now,
            authorId = "user123"
        )

        val response = meme.toTransport()

        assertEquals("123", response.id)
        assertEquals(meme.title, response.title)
        assertEquals(meme.tags, response.tags)
        assertEquals(meme.image, response.imageUrl)
    }

    @Test
    fun `should map context to transport MemeResponse`() {
        val context = CommonContext()
        val now = Instant.now()
        context.memeResponse = Meme(
            id = MemeId("123"),
            title = "Ответный мем",
            tags = listOf("response", "test"),
            image = "/uploads/response.jpg",
            createdAt = now
        )
        context.command = Command.CREATE

        val response = context.toTransportMemeResponse()

        assertEquals("123", response.id)
        assertEquals("Ответный мем", response.title)
        assertEquals(listOf("response", "test"), response.tags)
        assertEquals("/uploads/response.jpg", response.imageUrl)
    }

    @Test
    fun `should map context to transport MemeListResponse`() {
        val context = CommonContext()
        context.memesResponse = listOf(
            Meme(id = MemeId("1"), title = "Мем 1"),
            Meme(id = MemeId("2"), title = "Мем 2")
        )

        val response = context.toTransportMemeListResponse()

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

        val responses = memes.toTransportList()

        assertEquals(2, responses.size)
        assertEquals("1", responses[0].id)
        assertEquals("Мем 1", responses[0].title)
        assertEquals("2", responses[1].id)
        assertEquals("Мем 2", responses[1].title)
    }
}