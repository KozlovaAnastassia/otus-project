package ru.otus.otuskotlin.biz.stubs

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.stubs.MemeStubs
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MemeStubs.SUCCESS && command == MemeCommand.READ && state == MemeState.RUNNING }
    handle {
        state = MemeState.FINISHING
        memeResponse = Meme(
            id = memeRequest.id,
            title = "Stub мем ${memeRequest.id}",
            tags = listOf("stub", "read"),
            image = "/uploads/stub.jpg",
            createdAt = Clock.System.now()
        )
    }
}