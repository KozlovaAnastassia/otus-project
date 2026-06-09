package ru.otus.otuskotlin.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MemeId(private val id: String) {
    fun asString(): String = id

    fun isEmpty(): Boolean = id.isEmpty()
    fun isNotEmpty(): Boolean = id.isNotEmpty()

    companion object {
        val NONE = MemeId("")
    }
}