package ru.otus.otuskotlin.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*

class BizValidationSearchTest : BaseBizValidationTest() {
    override val command = MemeCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = MemeContext(
            command = command,
            state = MemeState.NONE,
            workMode = MemeWorkMode.TEST,
            memeFilterRequest = MemeFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MemeState.FAILING, ctx.state)
    }
}