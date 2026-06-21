package ru.otus.otuskotlin.biz.validation

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.chain
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.validation(block: ICorChainDsl<MemeContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == MemeState.RUNNING }
}

fun ICorChainDsl<MemeContext>.finishMemeValidation(title: String) = worker {
    this.title = title
    on { state == MemeState.RUNNING }
    handle {
        memeValidated = memeValidating
    }
}

fun ICorChainDsl<MemeContext>.finishMemeFilterValidation(title: String) = worker {
    this.title = title
    on { state == MemeState.RUNNING }
    handle {
        memeFilterValidated = memeFilterValidating
    }
}