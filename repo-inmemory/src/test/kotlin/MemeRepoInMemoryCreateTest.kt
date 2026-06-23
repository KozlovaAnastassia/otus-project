package ru.otus.otuskotlin.repo.inmemory

import ru.otus.otuskotlin.repo.tests.RepoMemeCreateTest
import java.util.concurrent.atomic.AtomicLong

class MemeRepoInMemoryCreateTest : RepoMemeCreateTest() {
    override val repo = MemeRepoInMemory(
        idGenerator = AtomicLong(1L)
    )
}