package ru.otus.otuskotlin.api.v1

import ru.otus.otuskotlin.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = MemeCreateRequest(
        debug = MemeDebug(
            mode = MemeRequestDebugMode.STUB,
            stub = MemeRequestDebugStubs.BAD_TITLE
        ),
        meme = MemeCreateObject(
            title = "meme title",
            tags = listOf("tag1", "tag2"),
            image = "base64image"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"meme title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MemeCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"meme": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, MemeCreateRequest::class.java)

        assertEquals(null, obj.meme)
    }
}