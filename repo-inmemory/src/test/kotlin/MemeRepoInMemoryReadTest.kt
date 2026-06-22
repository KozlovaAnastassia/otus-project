package ru.otus.otuskotlin.repo.inmemory

import ru.otus.otuskotlin.repo.tests.RepoMemeReadTest

class MemeRepoInMemoryReadTest : RepoMemeReadTest() {
    override val repo = MemeRepoInMemory()
}