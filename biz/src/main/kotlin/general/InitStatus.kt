package ru.otus.otuskotlin.biz.general

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == MemeState.NONE }
    handle { state = MemeState.RUNNING }
}