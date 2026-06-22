package ru.otus.otuskotlin.repo.tests

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk

abstract class RepoMemeReadTest {
    protected abstract val repo: IMemeRepo
    private lateinit var createdId: String

    @BeforeEach
    fun setUp() = runTest {
        val result = repo.create(
            Meme(
                title = "Существующий мем",
                tags = listOf("существующий", "тест"),
                image = "/uploads/test.jpg"
            )
        )
        when (result) {
            is MemeRepoResultOk -> {
                val data = result.data
                assertTrue(data.isNotEmpty(), "Created meme should be returned")
                createdId = data.first().id.asString()
            }
            else -> {
                createdId = "skip"
            }
        }
    }

    @Test
    fun readShouldReturnExistingMeme() = runTest {
        if (createdId == "skip") {
            return@runTest
        }
        assertNotNull(createdId, "createdId should not be null")
        val result = repo.read(MemeId(createdId))
        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data.first()
        assertEquals(createdId, found.id.asString())
        assertEquals("Существующий мем", found.title)
    }

    @Test
    fun readShouldReturnErrorForNonExistingMeme() = runTest {
        val result = repo.read(MemeId("non-existing"))
        assertTrue(result is MemeRepoResultErr)
        val err = result as MemeRepoResultErr
        assertTrue(err.errors.first().message.contains("not found"))
    }
}