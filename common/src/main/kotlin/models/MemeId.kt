package ru.otus.otuskotlin.common.models

import kotlin.jvm.JvmInline
import kotlin.random.Random

@JvmInline
value class MemeId(private val id: String) {
    fun asString(): String = id

    fun isEmpty(): Boolean = id.isEmpty()
    fun isNotEmpty(): Boolean = id.isNotEmpty()

    companion object {
        val NONE = MemeId("")

        fun generate(): MemeId = MemeId(
            Random.nextLong().toString(36) +
                    Random.nextLong().toString(36)
        )
    }
}