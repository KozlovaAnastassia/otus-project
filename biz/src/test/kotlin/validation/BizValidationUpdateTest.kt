package ru.otus.otuskotlin.biz

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*

class ValidationUpdateTest {

    private val processor = MemeProcessor()

    @Test
    fun correctTitle() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.UPDATE
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(
                id = MemeId("123"),
                title = "Valid Title"
            )
        }
        processor.exec(ctx)
        ctx.errors.forEach { println("error: $it") }
        assertEquals(0, ctx.errors.size)
        assertEquals("Valid Title", ctx.memeValidated.title)
    }

    @Test
    fun trimTitle() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.UPDATE
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(
                id = MemeId("123"),
                title = "  Trimmed Title  "
            )
        }
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertEquals("Trimmed Title", ctx.memeValidated.title)
    }

    @Test
    fun emptyTitle() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.UPDATE
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(
                id = MemeId("123"),
                title = ""
            )
        }
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty(), "Expected validation error for empty title")
        val error = ctx.errors.firstOrNull()
        assertEquals("title", error?.field)
    }

    @Test
    fun badSymbolsTitle() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.UPDATE
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(
                id = MemeId("123"),
                title = "!@#$%^&*()"
            )
        }
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty(), "Expected validation error for bad title symbols")
        val error = ctx.errors.firstOrNull()
        assertEquals("title", error?.field)
    }

    @Test
    fun correctId() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.UPDATE
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(
                id = MemeId("123"),
                title = "Valid Title"
            )
        }
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertEquals(MemeId("123"), ctx.memeValidated.id)
    }

    @Test
    fun trimId() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.UPDATE
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(
                id = MemeId("  123  "),
                title = "Valid Title"
            )
        }
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertEquals(MemeId("123"), ctx.memeValidated.id)
    }
}