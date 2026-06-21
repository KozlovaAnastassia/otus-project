package ru.otus.otuskotlin.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.rootChain

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeValidating = Meme(title = ""))
        chain.exec(ctx)
        assertEquals(MemeState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeValidating = Meme(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(MemeState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MemeContext(state = MemeState.RUNNING, memeValidating = Meme(title = "Ж"))
        chain.exec(ctx)
        assertEquals(MemeState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}