package ru.otus.otuskotlin.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MemeLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MemeLock("")
    }
}
