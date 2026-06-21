package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.chain

fun ICorChainDsl<MemeContext>.stubs(title: String, block: ICorChainDsl<MemeContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MemeWorkMode.STUB && state == MemeState.RUNNING }
}
