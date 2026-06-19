package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubValidationBadSearchString(title: String) = worker {
    this.title = title
    on { stubCase == MemeStubs.BAD_SEARCH_STRING && command == MemeCommand.SEARCH && state == MemeState.RUNNING }
    handle {
        errors.add(
            MemeError(
                code = "validation-search",
                field = "searchString",
                message = "Wrong search string"
            )
        )
        state = MemeState.FAILING
    }
}