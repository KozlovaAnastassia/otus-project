package ru.otus.otuskotlin.biz.validation

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.helpers.errorValidation
import ru.otus.otuskotlin.common.helpers.fail
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { memeValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<MemeContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { memeValidating.title.isNotEmpty() && !memeValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}