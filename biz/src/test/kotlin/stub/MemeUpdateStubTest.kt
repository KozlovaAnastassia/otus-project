package ru.otus.otuskotlin.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs

class MemeUpdateStubTest {

    private val processor = MemeProcessor()
    val id = MemeId("777")
    val title = "title 777"
    val tags = listOf("update", "test")
    val image = "base64update"

    @Test
    fun update() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.UPDATE,
            state = MemeState.NONE,
            workMode = MemeWorkMode.STUB,
            stubCase = MemeStubs.SUCCESS,
            memeRequest = Meme(
                id = id,
                title = title,
                tags = tags,
                image = image
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.memeResponse.id)
        assertEquals(title, ctx.memeResponse.title)
        assertEquals(tags, ctx.memeResponse.tags)
        assertEquals(image, ctx.memeResponse.image)
    }

    @Test
    fun badId() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.UPDATE,
            state = MemeState.NONE,
            workMode = MemeWorkMode.STUB,
            stubCase = MemeStubs.BAD_ID,
            memeRequest = Meme(),
        )
        processor.exec(ctx)
        assertEquals(Meme(), ctx.memeResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.UPDATE,
            state = MemeState.NONE,
            workMode = MemeWorkMode.TEST,
            stubCase = MemeStubs.BAD_TITLE,
            memeRequest = Meme(
                id = id,
                title = "",
                tags = tags,
                image = image
            ),
        )
        processor.exec(ctx)
        assertEquals(Meme(), ctx.memeResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.UPDATE,
            state = MemeState.NONE,
            workMode = MemeWorkMode.STUB,
            stubCase = MemeStubs.BAD_SEARCH_STRING,
            memeRequest = Meme(
                id = id,
                title = title,
                tags = tags,
                image = image
            ),
        )
        processor.exec(ctx)
        assertEquals(Meme(), ctx.memeResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}