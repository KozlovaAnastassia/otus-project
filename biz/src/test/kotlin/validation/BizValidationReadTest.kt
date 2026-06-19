package ru.otus.otuskotlin.biz.validation

import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.MemeCommand

class BizValidationReadTest : BaseBizValidationTest() {
    override val command = MemeCommand.READ

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
}