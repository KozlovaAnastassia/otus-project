package ru.otus.otuskotlin.common.models

data class MemeError(
    val code: String = "",
    val field: String = "",
    val message: String = "",
)
