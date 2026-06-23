package ru.otus.otuskotlin.repo.tests

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk
import ru.otus.otuskotlin.repo.inmemory.MemeRepoInMemory

abstract class RepoMemeDeleteTest {
    protected abstract val repo: IMemeRepo
    private lateinit var createdId: String

    @BeforeEach
    fun setUp() = runTest {
        if (repo is MemeRepoInMemory) {
            (repo as MemeRepoInMemory).clear()
        }

        val result = repo.create(Meme(title = "Мем для удаления"))
        if (result is MemeRepoResultOk) {
            createdId = result.data.first().id.asString()
        }
    }

    @Test
    fun deleteShouldRemoveMeme() = runTest {
        val result = repo.delete(MemeId(createdId))
        assertTrue(result is MemeRepoResultOk)
        val readResult = repo.read(MemeId(createdId))
        assertTrue(readResult is MemeRepoResultErr)
    }

    @Test
    fun deleteShouldReturnErrorForNonExistingMeme() = runTest {
        val result = repo.delete(MemeId("non-existing"))
        assertTrue(result is MemeRepoResultErr)
        val err = result as MemeRepoResultErr
        assertTrue(err.errors.first().message.contains("not found"))
    }
}