package ru.otus.otuskotlin.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs

class MemeSearchStubTest {

    private val processor = MemeProcessor()
    val filter = MemeFilter(searchString = "stub")

    @Test
    fun search() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.SEARCH,
            state = MemeState.NONE,
            workMode = MemeWorkMode.STUB,
            stubCase = MemeStubs.SUCCESS,
            memeFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.memesResponse.isNotEmpty())
        val first = ctx.memesResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains("Stub"))
        assertEquals(listOf("stub", "search"), first.tags)
    }

    @Test
    fun badSearchString() = runTest {
        val ctx = MemeContext().apply {
            command = MemeCommand.SEARCH
            state = MemeState.NONE
            workMode = MemeWorkMode.STUB
            stubCase = MemeStubs.BAD_SEARCH_STRING
            memeFilterRequest = MemeFilter(searchString = "")
        }
        processor.exec(ctx)
        assertTrue(ctx.errors.isNotEmpty(), "Expected validation error for bad search string")
        val error = ctx.errors.firstOrNull()
        assertEquals("stub", error?.field)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MemeContext(
            command = MemeCommand.SEARCH,
            state = MemeState.NONE,
            workMode = MemeWorkMode.STUB,
            stubCase = MemeStubs.BAD_TITLE,
            memeFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Meme(), ctx.memeResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}