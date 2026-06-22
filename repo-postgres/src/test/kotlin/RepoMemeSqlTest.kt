package ru.otus.otuskotlin.repo.postgres

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import ru.otus.otuskotlin.common.models.*
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import java.sql.DriverManager

class RepoMemeSqlLocalTest {

    companion object {
        private lateinit var repo: RepoMemeSql

        @BeforeAll
        @JvmStatic
        fun setUp() {
            val conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/memes_test",
                "postgres",
                "postgres"
            )

            val stmt = conn.createStatement()
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS memes (
                    id VARCHAR(255) PRIMARY KEY,
                    title VARCHAR(255),
                    tags TEXT,
                    image TEXT,
                    image_url TEXT,
                    created_at VARCHAR(255),
                    author_id VARCHAR(255),
                    lock VARCHAR(255) NOT NULL,
                    visibility VARCHAR(50) NOT NULL
                )
            """.trimIndent())
            stmt.close()
            conn.close()

            val properties = SqlProperties(
                host = "localhost",
                port = 5432,
                user = "postgres",
                password = "postgres",
                database = "memes_test"
            )
            repo = RepoMemeSql(properties)
        }
    }

    @BeforeEach
    fun cleanUp() = runTest {
        repo.clear()
    }

    @Test
    fun createShouldSaveMeme() = runTest {
        val meme = Meme(
            title = "Тестовый мем",
            tags = listOf("тест", "кот"),
            image = "base64image",
            createdAt = kotlinx.datetime.Clock.System.now()
        )

        val result = repo.create(meme)
        assertTrue(result is MemeRepoResultOk)
        val created = (result as MemeRepoResultOk).data.first()
        assertNotNull(created.id.asString())
        assertEquals(meme.title, created.title)
        assertEquals(meme.tags, created.tags)
        assertEquals(meme.image, created.image)
    }

    @Test
    fun createShouldPreserveIdWhenProvided() = runTest {
        val testId = "test-123"
        val meme = Meme(
            id = MemeId(testId),
            title = "Мем с ID",
            tags = listOf("id", "test")
        )

        val result = repo.create(meme)

        assertTrue(result is MemeRepoResultOk)
        val created = (result as MemeRepoResultOk).data.first()
        assertEquals(testId, created.id.asString())
        assertEquals(meme.title, created.title)
    }

    @Test
    fun readShouldReturnExistingMeme() = runTest {
        val testId = "test-123"
        val meme = Meme(
            id = MemeId(testId),
            title = "Существующий мем",
            tags = listOf("существующий")
        )
        repo.create(meme)

        val result = repo.read(MemeId(testId))

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data.first()
        assertEquals(testId, found.id.asString())
        assertEquals(meme.title, found.title)
        assertEquals(meme.tags, found.tags)
    }

    @Test
    fun readShouldReturnErrorForNonExistingMeme() = runTest {
        val result = repo.read(MemeId("non-existing"))

        assertTrue(result is MemeRepoResultErr)
        val err = result as MemeRepoResultErr
        assertEquals("not-found", err.errors.first())
    }

    @Test
    fun updateShouldModifyExistingMeme() = runTest {
        val testId = "test-123"
        repo.create(Meme(id = MemeId(testId), title = "Старый заголовок"))

        val updated = Meme(
            id = MemeId(testId),
            title = "Новый заголовок",
            tags = listOf("обновлено")
        )
        val result = repo.update(updated)

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data.first()
        assertEquals("Новый заголовок", found.title)
        assertEquals(listOf("обновлено"), found.tags)
    }

    @Test
    fun updateShouldReturnErrorForNonExistingMeme() = runTest {
        val result = repo.update(
            Meme(
                id = MemeId("non-existing"),
                title = "Несуществующий"
            )
        )

        assertTrue(result is MemeRepoResultErr)
        val err = result as MemeRepoResultErr
        assertEquals("not-found", err.errors.first())
    }

    @Test
    fun deleteShouldRemoveMeme() = runTest {
        val testId = "test-123"
        repo.create(Meme(id = MemeId(testId), title = "Удаляемый мем"))

        val result = repo.delete(MemeId(testId))

        assertTrue(result is MemeRepoResultOk)
        val readResult = repo.read(MemeId(testId))
        assertTrue(readResult is MemeRepoResultErr)
    }

    @Test
    fun deleteShouldReturnErrorForNonExistingMeme() = runTest {
        val result = repo.delete(MemeId("non-existing"))

        assertTrue(result is MemeRepoResultErr)
        val err = result as MemeRepoResultErr
        assertEquals("not-found", err.errors.first())
    }

    @Test
    fun searchShouldFindByTitle() = runTest {
        repo.create(Meme(title = "Кот в сапогах", tags = listOf("кот")))
        repo.create(Meme(title = "Собака на сене", tags = listOf("собака")))

        val result = repo.search(MemeFilter(searchString = "кот"))

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertEquals(1, found.size)
        assertEquals("Кот в сапогах", found.first().title)
    }

    @Test
    fun searchShouldFindByTags() = runTest {
        repo.create(Meme(title = "Мем с котом", tags = listOf("кот", "смешное")))
        repo.create(Meme(title = "Мем с собакой", tags = listOf("собака", "прикол")))

        val result = repo.search(MemeFilter(tags = listOf("кот")))

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertEquals(1, found.size)
        assertEquals("Мем с котом", found.first().title)
    }

    @Test
    fun searchShouldReturnAllWhenFilterEmpty() = runTest {
        repo.create(Meme(title = "Мем 1", tags = listOf("тест")))
        repo.create(Meme(title = "Мем 2", tags = listOf("тест")))

        val result = repo.search(MemeFilter())

        assertTrue(result is MemeRepoResultOk)
        val found = (result as MemeRepoResultOk).data
        assertEquals(2, found.size)
    }
}