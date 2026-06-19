package ru.otus.otuskotlin.biz.validation

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.helpers.errorValidation
import ru.otus.otuskotlin.common.helpers.fail
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { memeValidating.id.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<MemeContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { memeValidating.id != MemeId.NONE && !memeValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = memeValidating.id.asString()
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}