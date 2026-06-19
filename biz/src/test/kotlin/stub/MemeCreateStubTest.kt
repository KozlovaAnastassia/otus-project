package ru.otus.otuskotlin.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs

class MemeCreateStubTest {

    private val processor = MemeProcessor()
    private val id = MemeId("666")
    private val title = "Test Meme"
    private val tags = listOf("test", "stub")
    private val image = "base64image"

    @Test
    fun create() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.CREATE
            state = MemeState.NONE
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.SUCCESS
            memeRequest = Meme(
                id = id,
                title = title,
                tags = tags,
                image = image
            )
        }

        processor.exec(ctx)
        assertNotEquals(Meme(), ctx.memeResponse)
        assertEquals(MemeId("stub-123"), ctx.memeResponse.id)
        assertEquals(title, ctx.memeResponse.title)
        assertEquals(tags, ctx.memeResponse.tags)
        assertEquals(image, ctx.memeResponse.image)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.CREATE
            state = MemeState.NONE
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.BAD_TITLE
            memeRequest = Meme(
                title = "",
                tags = tags,
                image = image
            )
        }
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.CREATE
            state = MemeState.NONE
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.NONE
            memeRequest = Meme(
                title = title,
                tags = tags,
                image = image
            )
        }
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}