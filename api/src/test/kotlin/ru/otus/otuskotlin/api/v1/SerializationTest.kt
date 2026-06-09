package ru.otus.otuskotlin.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.models.*
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
    fun `should serialize and deserialize MemeCreateRequest with all fields`() {
        val original = MemeCreateRequest(
            requestType = "create",
            meme = MemeCreateObject(
                title = "Тестовый мем",
                tags = listOf("кот", "смешное", "мем"),
                image = "base64EncodedImageString"
            )
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeCreateRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.meme?.title, deserialized.meme?.title)
        assertEquals(original.meme?.tags, deserialized.meme?.tags)
        assertEquals(original.meme?.image, deserialized.meme?.image)
    }

    @Test
    fun `should serialize and deserialize MemeCreateRequest with only required fields`() {
        val original = MemeCreateRequest(
            requestType = "create",
            meme = MemeCreateObject(
                title = "Минимальный мем"
            )
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeCreateRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.meme?.title, deserialized.meme?.title)
        assertNull(deserialized.meme?.tags)
        assertNull(deserialized.meme?.image)
    }

    @Test
    fun `should handle empty tags list in MemeCreateRequest`() {
        val original = MemeCreateRequest(
            requestType = "create",
            meme = MemeCreateObject(
                title = "Мем с пустыми тегами",
                tags = emptyList()
            )
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeCreateRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.meme?.title, deserialized.meme?.title)
        assertTrue(deserialized.meme?.tags.isNullOrEmpty())
    }

    @Test
    fun `should serialize and deserialize MemeReadRequest`() {
        val original = MemeReadRequest(
            requestType = "read",
            meme = MemeReadObject(id = "123")
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeReadRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.meme?.id, deserialized.meme?.id)
    }

    @Test
    fun `should serialize and deserialize MemeUpdateRequest`() {
        val original = MemeUpdateRequest(
            requestType = "update",
            meme = MemeUpdateObject(
                id = "123",
                title = "Обновлённый мем",
                tags = listOf("обновление", "тест")
            )
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeUpdateRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.meme?.id, deserialized.meme?.id)
        assertEquals(original.meme?.title, deserialized.meme?.title)
        assertEquals(original.meme?.tags, deserialized.meme?.tags)
    }

    @Test
    fun `should serialize and deserialize MemeDeleteRequest`() {
        val original = MemeDeleteRequest(
            requestType = "delete",
            meme = MemeDeleteObject(id = "123")
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeDeleteRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.meme?.id, deserialized.meme?.id)
    }

    @Test
    fun `should serialize and deserialize MemeSearchRequest`() {
        val original = MemeSearchRequest(
            requestType = "search",
            memeFilter = MemeSearchFilter(
                searchString = "кот",
                tags = listOf("смешное", "мем")
            )
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeSearchRequest>(json)

        assertEquals(original.requestType, deserialized.requestType)
        assertEquals(original.memeFilter?.searchString, deserialized.memeFilter?.searchString)
        assertEquals(original.memeFilter?.tags, deserialized.memeFilter?.tags)
    }

    @Test
    fun `should serialize and deserialize MemeResponseObject with all fields`() {
        val now = OffsetDateTime.now()
        val original = MemeResponseObject(
            title = "Ответный мем",
            tags = listOf("ответ", "тест"),
            id = "123",
            imageUrl = "/uploads/meme123.jpg",
            createdAt = now
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeResponseObject>(json)

        assertEquals(original.id, deserialized.id)
        assertEquals(original.title, deserialized.title)
        assertEquals(original.tags, deserialized.tags)
        assertEquals(original.imageUrl, deserialized.imageUrl)
        assertEquals(original.createdAt?.toEpochSecond(), deserialized.createdAt?.toEpochSecond())
    }

    @Test
    fun `should handle MemeResponseObject with null optional fields`() {
        val original = MemeResponseObject(
            title = "Мем без ID"
        )

        val json = objectMapper.writeValueAsString(original)
        val deserialized = objectMapper.readValue<MemeResponseObject>(json)

        assertNull(deserialized.id)
        assertEquals(original.title, deserialized.title)
        assertNull(deserialized.tags)
        assertNull(deserialized.imageUrl)
        assertNull(deserialized.createdAt)
    }

    @Test
    fun `should produce valid JSON format for MemeCreateRequest`() {
        val request = MemeCreateRequest(
            requestType = "create",
            meme = MemeCreateObject(
                title = "JSON тест",
                tags = listOf("json", "формат"),
                image = "data:image/png;base64,xxx"
            )
        )

        val json = objectMapper.writeValueAsString(request)

        assertTrue(json.contains("\"requestType\":\"create\""))
        assertTrue(json.contains("\"title\":\"JSON тест\""))
        assertTrue(json.contains("\"tags\":[\"json\",\"формат\"]"))
        assertTrue(json.contains("\"image\":\"data:image/png;base64,xxx\""))
    }

    @Test
    fun `should deserialize MemeCreateRequest from valid JSON string`() {
        val json = """
            {
                "requestType": "create",
                "meme": {
                    "title": "Из JSON",
                    "tags": ["парсинг", "тест"],
                    "image": "base64data"
                }
            }
        """.trimIndent()

        val deserialized = objectMapper.readValue<MemeCreateRequest>(json)

        assertEquals("create", deserialized.requestType)
        assertEquals("Из JSON", deserialized.meme?.title)
        assertEquals(listOf("парсинг", "тест"), deserialized.meme?.tags)
        assertEquals("base64data", deserialized.meme?.image)
    }
}