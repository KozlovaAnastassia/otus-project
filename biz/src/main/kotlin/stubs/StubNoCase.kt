package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == MemeState.RUNNING }
    handle {
        errors.add(
            MemeError(
                code = "validation",
                field = "stub",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
        state = MemeState.FAILING
    }
}