package ru.otus.otuskotlin.common.models

import java.time.Instant

data class Meme(
    val id: MemeId = MemeId.NONE,
    val title: String = "",
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val createdAt: Instant = Instant.now(),
    val authorId: String = ""
)

@JvmInline
value class MemeId(val value: String) {
    companion object {
        val NONE = MemeId("")
    }

    override fun toString(): String = value
}