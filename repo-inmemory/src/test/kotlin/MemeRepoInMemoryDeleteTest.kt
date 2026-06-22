package ru.otus.otuskotlin.repo.inmemory

import ru.otus.otuskotlin.repo.tests.RepoMemeDeleteTest
import java.util.concurrent.atomic.AtomicLong

class MemeRepoInMemoryDeleteTest : RepoMemeDeleteTest() {
    override val repo = MemeRepoInMemory(
        idGenerator = AtomicLong(1L)
    )
}