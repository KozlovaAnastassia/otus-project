package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MemeStubs.SUCCESS && command == MemeCommand.SEARCH && state == MemeState.RUNNING }
    handle {
        state = MemeState.FINISHING
        memesResponse = mutableListOf(
            Meme(id = MemeId("stub-1"), title = "Stub мем 1", tags = listOf("stub", "search"), createdAt = Clock.System.now()),
            Meme(id = MemeId("stub-2"), title = "Stub мем 2", tags = listOf("stub", "search"), createdAt = Clock.System.now()),
            Meme(id = MemeId("stub-3"), title = "Stub мем 3", tags = listOf("stub", "search", "test"), createdAt = Clock.System.now())
        )
    }
}