package ru.otus.otuskotlin.common.repo

import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeError
import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.models.MemeLock
import ru.otus.otuskotlin.common.repo.exceptions.RepoConcurrencyException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MemeId): MemeRepoResultErr = MemeRepoResultErr(
    MemeError(
        code = "$ERROR_GROUP_REPO-not-found",
        field = "id",
        message = "Meme with ID: ${id} is not found"
    )
)

fun errorEmptyId(): MemeRepoResultErr = MemeRepoResultErr(
    MemeError(
        code = "$ERROR_GROUP_REPO-empty-id",
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorEmptyLock(id: MemeId): MemeRepoResultErr = MemeRepoResultErr(
    MemeError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        field = "lock",
        message = "Lock for Meme ${id} is empty"
    )
)

fun errorRepoConcurrency(
    oldMeme: Meme,
    expectedLock: MemeLock,
    exception: Exception = RepoConcurrencyException(
        id = oldMeme.id,
        expectedLock = expectedLock,
        actualLock = oldMeme.lock
    )
): MemeRepoResultErrWithData = MemeRepoResultErrWithData(
    data = listOf(oldMeme),
    errors = listOf(
        MemeError(
            code = "$ERROR_GROUP_REPO-concurrency",
            field = "lock",
            message = "The meme with ID ${oldMeme.id} has been changed concurrently"
        )
    )
)

fun errorDb(exception: Exception): MemeRepoResultErr = MemeRepoResultErr(
    MemeError(
        code = "$ERROR_GROUP_REPO-db-error",
        message = "Database error: ${exception.message}"
    )
)