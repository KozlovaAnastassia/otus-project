package ru.otus.otuskotlin.repo.inmemory

import ru.otus.otuskotlin.repo.tests.RepoMemeUpdateTest
import java.util.concurrent.atomic.AtomicLong

class MemeRepoInMemoryUpdateTest : RepoMemeUpdateTest() {
    override val repo = MemeRepoInMemory(
        idGenerator = AtomicLong(1L)
    )
}