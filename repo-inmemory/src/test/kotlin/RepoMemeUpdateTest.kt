package ru.otus.otuskotlin.repo.tests

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk

abstract class RepoMemeUpdateTest {
    protected abstract val repo: IMemeRepo
    private lateinit var createdId: String
    private lateinit var createdLock: String
    protected val lockNew = "lock-2222222222"

    @BeforeEach
    fun setUp() = runTest {
        val result = repo.create(
            Meme(
                title = "Старый заголовок",
                tags = listOf("старый")
            )
        )
        if (result is MemeRepoResultOk) {
            val meme = result.data.first()
            createdId = meme.id.asString()
            createdLock = meme.lock.asString()
        }
    }

    @Test
    fun updateShouldModifyExistingMeme() = runTest {
        val updated = Meme(
            id = MemeId(createdId),
            title = "Новый заголовок",
            tags = listOf("новый", "тег"),
            lock = MemeLock(lockNew)
        )
        val result = repo.update(updated)

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data.first()
        assertEquals(createdId, found.id.asString())
        assertEquals("Новый заголовок", found.title)
        assertNotEquals(createdLock, found.lock.asString())
    }

    @Test
    fun updateShouldReturnErrorForNonExistingMeme() = runTest {
        val meme = Meme(
            id = MemeId("non-existing"),
            title = "Несуществующий",
            lock = MemeLock(lockNew)
        )
        val result = repo.update(meme)
        assertTrue(result is MemeRepoResultErr)
        val err = result as MemeRepoResultErr
        assertTrue(err.errors.first().message.contains("not found"))
    }
}