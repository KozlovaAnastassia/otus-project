package ru.otus.otuskotlin.common.helpers

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeError
import ru.otus.otuskotlin.common.models.MemeState

fun errorValidation(
    field: String,
    violationCode: String,
    description: String
): MemeError = MemeError(
    code = "validation-$violationCode",
    field = field,
    message = description
)

fun MemeContext.fail(error: MemeError) {
    errors.add(error)
    state = MemeState.FAILING
}
