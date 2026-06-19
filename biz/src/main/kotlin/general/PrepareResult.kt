package ru.otus.otuskotlin.biz.general

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.prepareResult(title: String) = worker {
    this.title = title
    on { state == MemeState.RUNNING }
    handle {
        state = when (state) {
            MemeState.RUNNING -> MemeState.FINISHING
            else -> state
        }
    }
}
