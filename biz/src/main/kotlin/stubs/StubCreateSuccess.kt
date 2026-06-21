package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MemeStubs.SUCCESS && command == MemeCommand.CREATE && state == MemeState.RUNNING }
    handle {
        state = MemeState.FINISHING
        memeResponse = Meme(
            id = MemeId("stub-123"),
            title = memeRequest.title.takeIf { it.isNotBlank() } ?: "Stub мем",
            tags = memeRequest.tags,
            image = memeRequest.image,
            createdAt = Clock.System.now()
        )
    }
}