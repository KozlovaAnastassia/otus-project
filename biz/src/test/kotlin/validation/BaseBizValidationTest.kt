package ru.otus.otuskotlin.biz.validation

import ru.otus.otuskotlin.biz.MemeProcessor
import ru.otus.otuskotlin.common.models.MemeCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MemeCommand
    protected val processor by lazy { MemeProcessor() }
}