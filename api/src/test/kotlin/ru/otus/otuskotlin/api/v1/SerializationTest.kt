package ru.otus.otuskotlin.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.models.CreateMemeRequest
import ru.otus.otuskotlin.api.v1.models.MemeResponse
import java.time.OffsetDateTime

class SerializationTest {

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Test
    fun `should serialize and deserialize CreateMemeRequest with all fields`() {
        // Given
        val original = CreateMemeRequest(
            title = "Тестовый мем",
            tags = listOf("кот", "смешное", "мем"),
            image = "base64EncodedImageString"
        )

        // When
        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<CreateMemeRequest>(json)

        // Then
        assertEquals(original.title, deserialized.title, "Title should match")
        assertEquals(original.tags, deserialized.tags, "Tags should match")
        assertEquals(original.image, deserialized.image, "Image should match")
    }

    @Test
    fun `should serialize and deserialize CreateMemeRequest with only required fields`() {
        // Given
        val original = CreateMemeRequest(
            title = "Минимальный мем",
            tags = null,
            image = null
        )

        // When
        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<CreateMemeRequest>(json)

        // Then
        assertEquals(original.title, deserialized.title, "Title should match")
        assertNull(deserialized.tags, "Tags should be null")
        assertNull(deserialized.image, "Image should be null")
    }

    @Test
    fun `should handle empty tags list`() {
        // Given
        val original = CreateMemeRequest(
            title = "Мем с пустыми тегами",
            tags = emptyList(),
            image = null
        )

        // When
        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<CreateMemeRequest>(json)

        // Then
        assertEquals(original.title, deserialized.title)
        assertTrue(deserialized.tags.isNullOrEmpty(), "Tags should be empty")
    }

    @Test
    fun `should serialize and deserialize MemeResponse with all fields`() {
        // Given
        val now = OffsetDateTime.now()
        val original = MemeResponse(
            id = 123L,
            title = "Ответный мем",
            tags = listOf("ответ", "тест"),
            imageUrl = "/uploads/meme123.jpg",
            createdAt = now
        )

        // When
        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeResponse>(json)

        // Then
        assertEquals(original.id, deserialized.id, "Id should match")
        assertEquals(original.title, deserialized.title, "Title should match")
        assertEquals(original.tags, deserialized.tags, "Tags should match")
        assertEquals(original.imageUrl, deserialized.imageUrl, "ImageUrl should match")
        assertEquals(original.createdAt?.toEpochSecond(), deserialized.createdAt?.toEpochSecond(), "CreatedAt should match")
    }

    @Test
    fun `should handle MemeResponse with null optional fields`() {
        // Given
        val original = MemeResponse(
            id = null,
            title = "Мем без ID",
            tags = null,
            imageUrl = null,
            createdAt = null
        )

        // When
        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeResponse>(json)

        // Then
        assertNull(deserialized.id, "Id should be null")
        assertEquals(original.title, deserialized.title, "Title should match")
        assertNull(deserialized.tags, "Tags should be null")
        assertNull(deserialized.imageUrl, "ImageUrl should be null")
        assertNull(deserialized.createdAt, "CreatedAt should be null")
    }

    @Test
    fun `should produce valid JSON format`() {
        // Given
        val request = CreateMemeRequest(
            title = "JSON тест",
            tags = listOf("json", "формат"),
            image = "data:image/png;base64,xxx"
        )

        // When
        val json = objectMapper.writeValueAsString(request)

        // Then
        assertTrue(json.contains("\"title\":\"JSON тест\""), "JSON should contain title")
        assertTrue(json.contains("\"tags\":[\"json\",\"формат\"]"), "JSON should contain tags")
        assertTrue(json.contains("\"image\":\"data:image/png;base64,xxx\""), "JSON should contain image")
    }

    @Test
    fun `should deserialize from valid JSON string`() {
        // Given
        val json = """
            {
                "title": "Из JSON",
                "tags": ["парсинг", "тест"],
                "image": "base64data"
            }
        """.trimIndent()

        // When
        val deserialized = objectMapper.readValue<CreateMemeRequest>(json)

        // Then
        assertEquals("Из JSON", deserialized.title)
        assertEquals(listOf("парсинг", "тест"), deserialized.tags)
        assertEquals("base64data", deserialized.image)
    }
}