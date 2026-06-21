package ru.otus.otuskotlin.biz.validation

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.helpers.errorValidation
import ru.otus.otuskotlin.common.helpers.fail
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.chain
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    on { state == MemeState.RUNNING }

    worker("Обрезка пустых символов") {
        memeFilterValidating.searchString = memeFilterValidating.searchString.trim()
    }

    worker {
        this.title = "Проверка кейса длины на 0-2 символа"
        on { state == MemeState.RUNNING && memeFilterValidating.searchString.length in (1..2) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }

    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        on { state == MemeState.RUNNING && memeFilterValidating.searchString.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
}