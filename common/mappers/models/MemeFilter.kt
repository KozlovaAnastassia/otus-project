package ru.otus.otuskotlin.common.models

data class MemeFilter(
    val searchString: String = "",
    val tags: List<String> = emptyList()
)