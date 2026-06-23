package ru.otus.otuskotlin.common.repo

import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeError

sealed interface MemeRepoResult {
    val data: List<Meme>?
    val errors: List<MemeError>?
}

data class MemeRepoResultOk(override val data: List<Meme>) : MemeRepoResult {
    constructor(data: Meme) : this(listOf(data))
    override val errors: List<MemeError>? = null
}

data class MemeRepoResultErr(override val errors: List<MemeError>) : MemeRepoResult {
    constructor(error: MemeError) : this(listOf(error))
    override val data: List<Meme>? = null
}

data class MemeRepoResultErrWithData(
    override val data: List<Meme>,
    override val errors: List<MemeError>
) : MemeRepoResult