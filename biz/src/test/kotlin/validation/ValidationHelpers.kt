package ru.otus.otuskotlin.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*

fun validationTitleCorrect(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(title = "abc")
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MemeState.FAILING, ctx.state)
    assertEquals("abc", ctx.memeValidated.title)
}

fun validationTitleTrim(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(title = " \n\t abc \t\n ")
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MemeState.FAILING, ctx.state)
    assertEquals("abc", ctx.memeValidated.title)
}

fun validationTitleEmpty(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(title = "")
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MemeState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
}

fun validationTitleSymbols(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(title = "!@#$%^&*(),.{}")
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MemeState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
}

fun validationIdCorrect(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(id = MemeId("123"))
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MemeState.FAILING, ctx.state)
}

fun validationIdTrim(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(id = MemeId(" \n\t 123 \n\t "))
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MemeState.FAILING, ctx.state)
}

fun validationIdEmpty(command: MemeCommand, processor: MemeProcessor) = runTest {
    val ctx = MemeContext(
        command = command,
        state = MemeState.NONE,
        workMode = MemeWorkMode.TEST,
        memeRequest = Meme(id = MemeId(""))
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MemeState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
}