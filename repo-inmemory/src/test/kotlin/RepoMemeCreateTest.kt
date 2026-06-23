package ru.otus.otuskotlin.repo.tests

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk

abstract class RepoMemeCreateTest {
    protected abstract val repo: IMemeRepo

    @Test
    fun createShouldSaveMeme() = runTest {
        val meme = Meme(
            title = "Тестовый мем",
            tags = listOf("тест", "кот"),
            image = "base64image"
        )

        val result = repo.create(meme)

        assertTrue(result is MemeRepoResultOk)
        val created = (result as MemeRepoResultOk).data.first()
        assertNotEquals(MemeId.NONE, created.id)
        assertEquals(meme.title, created.title)
        assertEquals(meme.tags, created.tags)
        assertEquals(meme.image, created.image)
        assertNotEquals(MemeLock.NONE, created.lock)
    }

    @Test
    fun createShouldSetIdAndLock() = runTest {
        val meme = Meme(title = "Мем с ID")

        val result = repo.create(meme)

        assertTrue(result is MemeRepoResultOk)
        val created = (result as MemeRepoResultOk).data.first()
        assertNotEquals(MemeId.NONE, created.id)
        assertNotEquals(MemeLock.NONE, created.lock)
    }
}