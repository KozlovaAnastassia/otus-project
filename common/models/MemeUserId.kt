package ru.otus.otuskotlin.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MemeUserId(private val id: String) {
    fun asString(): String = id

    companion object {
        val NONE = MemeId("")
    }
}