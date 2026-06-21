package ru.otus.otuskotlin.biz.general

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeCommand
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.chain

fun ICorChainDsl<MemeContext>.operation(
    title: String,
    command: MemeCommand,
    block: ICorChainDsl<MemeContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MemeState.RUNNING }
}
