package ru.otus.otuskotlin.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeFilter
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.rootChain

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeFilterValidating = MemeFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(MemeState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeFilterValidating = MemeFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(MemeState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeFilterValidating = MemeFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(MemeState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeFilterValidating = MemeFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(MemeState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeFilterValidating = MemeFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(MemeState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}