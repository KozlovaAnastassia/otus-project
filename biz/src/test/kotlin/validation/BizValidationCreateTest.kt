package ru.otus.otuskotlin.biz.validation

import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.MemeCommand

class BizValidationCreateTest : BaseBizValidationTest() {
    override val command: MemeCommand = MemeCommand.CREATE

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)
}