package ru.otus.otuskotlin.repo.tests

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk

abstract class RepoMemeSearchTest {
    protected abstract val repo: IMemeRepo

    @BeforeEach
    fun setUp() = runTest {
        repo.create(Meme(title = "Кот в сапогах", tags = listOf("кот", "сказка")))
        repo.create(Meme(title = "Собака на сене", tags = listOf("собака", "басня")))
        repo.create(Meme(title = "Кот и пёс", tags = listOf("кот", "собака")))
    }

    @Test
    fun searchShouldFindByTitle() = runTest {
        val result = repo.search(MemeFilter(searchString = "кот"))

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertEquals(2, found.size)
        assertTrue(found.all { it.title.contains("кот", ignoreCase = true) })
    }

    @Test
    fun searchShouldFindByTags() = runTest {
        val result = repo.search(MemeFilter(tags = listOf("сказка")))

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertEquals(1, found.size)
        assertEquals("Кот в сапогах", found.first().title)
    }

    @Test
    fun searchShouldReturnAllWhenFilterEmpty() = runTest {
        val result = repo.search(MemeFilter())

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertEquals(3, found.size)
    }

    @Test
    fun searchShouldReturnEmptyWhenNoMatch() = runTest {
        val result = repo.search(MemeFilter(searchString = "динозавр"))

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertTrue(found.isEmpty())
    }
}