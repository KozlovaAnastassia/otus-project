package ru.otus.otuskotlin.api.v1

import ru.otus.otuskotlin.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = MemeCreateResponse(
        responseType = "create",
        result = "success",
        meme = MemeResponseObject(
            id = "123",
            title = "meme title",
            tags = listOf("tag1", "tag2"),
            imageUrl = "/uploads/meme.jpg",
            createdAt = "2024-01-01T12:00:00Z"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"meme title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"id\":\\s*\"123\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MemeCreateResponse

        assertEquals(response, obj)
    }

    @Test
    fun deserializeMemeCreateResponseWithNulls() {
        val json = """
            {
                "responseType": "create",
                "result": "error",
                "errors": [
                    {
                        "code": "VALIDATION_ERROR",
                        "message": "Title is required"
                    }
                ],
                "meme": null
            }
        """.trimIndent()

        val obj = apiV1Mapper.readValue(json, MemeCreateResponse::class.java)

        assertEquals("create", obj.responseType)
        assertEquals("error", obj.result)
        assertEquals(1, obj.errors?.size)
        assertEquals("VALIDATION_ERROR", obj.errors?.get(0)?.code)
        assertNull(obj.meme)
    }
}