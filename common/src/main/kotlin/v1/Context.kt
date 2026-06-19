package ru.otus.otuskotlin.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs

data class MemeContext(
    var command: MemeCommand = MemeCommand.NONE,
    var state: MemeState = MemeState.NONE,
    val errors: MutableList<MemeError> = mutableListOf(),

    var workMode: MemeWorkMode = MemeWorkMode.PROD,
    var stubCase: MemeStubs = MemeStubs.NONE,

    var requestId: MemeRequestId = MemeRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var memeRequest: Meme = Meme(),
    var memeFilterRequest: MemeFilter = MemeFilter(),

    var memeResponse: Meme = Meme(),
    var memesResponse: MutableList<Meme> = mutableListOf(),

    var memeValidating: Meme = Meme(),
    var memeValidated: Meme = Meme(),
    var memeFilterValidating: MemeFilter = MemeFilter(),
    var memeFilterValidated: MemeFilter = MemeFilter(),
)