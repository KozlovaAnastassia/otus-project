package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == MemeStubs.BAD_TITLE && command == MemeCommand.CREATE && state == MemeState.RUNNING }
    handle {
        errors.add(
            MemeError(
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
        state = MemeState.FAILING
    }
}
