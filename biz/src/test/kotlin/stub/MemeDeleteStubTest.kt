package ru.otus.otuskotlin.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs

class MemeDeleteStubTest {

    private val processor = MemeProcessor()
    val id = MemeId("666")

    @Test
    fun delete() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.DELETE
            state = MemeState.NONE
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.SUCCESS
            memeRequest = Meme(id = id)
        }
        processor.exec(ctx)
        assertNotEquals(Meme(), ctx.memeResponse, "memeResponse should not be empty")
    }

    @Test
    fun badId() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.READ
            state = MemeState.NONE
            workMode = MemeWorkMode.TEST
            memeRequest = Meme(id = MemeId(""))
        }
        processor.exec(ctx)

        assertTrue(ctx.errors.isNotEmpty(), "Validation should fail for empty ID")
        val error = ctx.errors.firstOrNull()
        assertEquals("id", error?.field)
        assertTrue(error?.message?.contains("empty") == true, "Error should mention empty")
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.DELETE,
            state = MemeState.NONE,
            workMode = MemeWorkMode.STUB,
            stubCase = MemeStubs.BAD_TITLE,
            memeRequest = Meme(
                id = id,
            ),
        )
        processor.exec(ctx)

        assertTrue(ctx.errors.isNotEmpty(), "Should return error for unknown stub")
        val error = ctx.errors.firstOrNull()
        assertEquals("stub", error?.field)
    }
}
