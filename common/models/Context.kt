package ru.otus.otuskotlin.common.models

data class CommonContext(
    var requestId: String = "",
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    var errors: List<Error> = emptyList(),

    var memeRequest: Meme? = null,
    var memeId: MemeId = MemeId.NONE,
    var memeTitle: String = "",
    var memeTags: List<String> = emptyList(),
    
    var memeResponse: Meme? = null,
    var memesResponse: List<Meme> = emptyList()
)

enum class Command {
    CREATE, READ, UPDATE, DELETE, SEARCH, NONE
}

enum class State {
    RUNNING, FINISHING, FAILING, NONE
}

data class Error(
    val code: String,
    val message: String,
    val field: String? = null
)