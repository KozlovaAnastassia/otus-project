package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == MemeStubs.BAD_ID && state == MemeState.RUNNING }
    handle {
        errors.add(
            MemeError(
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
        state = MemeState.FAILING
    }
}
