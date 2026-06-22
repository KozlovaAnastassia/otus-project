package ru.otus.otuskotlin.repo.inmemory

import ru.otus.otuskotlin.repo.tests.RepoMemeSearchTest

class MemeRepoInMemorySearchTest : RepoMemeSearchTest() {
    override val repo = MemeRepoInMemory()
}