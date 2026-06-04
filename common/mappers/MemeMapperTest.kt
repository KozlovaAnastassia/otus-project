package ru.otus.otuskotlin.common.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.models.CreateMemeRequest
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId
import java.time.Instant

class MemeMapperTest {

    @Test
    fun `should map CreateMemeRequest to internal Meme`() {
        val request = CreateMemeRequest(
            title = "Тестовый мем",
            tags = listOf("кот", "прикол"),
            image = "base64image"
        )

        val meme = request.toInternal()

        assertEquals(request.title, meme.title)
        assertEquals(request.tags, meme.tags)
        assertEquals(MemeId.NONE, meme.id)
        assertEquals("", meme.imageUrl)
    }

    @Test
    fun `should handle null values in CreateMemeRequest`() {
        val request = CreateMemeRequest(
            title = "Мем без тегов",
            tags = null,
            image = null
        )

        val meme = request.toInternal()

        assertEquals(request.title, meme.title)
        assertTrue(meme.tags.isEmpty())
    }

    @Test
    fun `should map internal Meme to transport MemeResponse`() {
        val now = Instant.now()
        val meme = Meme(
            id = MemeId("123"),
            title = "Внутренний мем",
            tags = listOf("test", "mapper"),
            imageUrl = "/uploads/test.jpg",
            createdAt = now,
            authorId = "user123"
        )

        val response = meme.toTransport()

        assertEquals(123L, response.id)
        assertEquals(meme.title, response.title)
        assertEquals(meme.tags, response.tags)
        assertEquals(meme.imageUrl, response.imageUrl)
    }

    @Test
    fun `should map list of memes to transport list`() {
        val memes = listOf(
            Meme(id = MemeId("1"), title = "Мем 1"),
            Meme(id = MemeId("2"), title = "Мем 2")
        )

        val responses = memes.toTransportList()

        assertEquals(2, responses.size)
        assertEquals("Мем 1", responses[0].title)
        assertEquals("Мем 2", responses[1].title)
    }
}