package ru.otus.otuskotlin.common.repo.exceptions

import ru.otus.otuskotlin.common.models.MemeId

open class RepoMemeException(
    @Suppress("unused")
    val memeId: MemeId,
    msg: String
) : RepoException(msg)