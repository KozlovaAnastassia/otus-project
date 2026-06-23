package ru.otus.otuskotlin.common.repo.exceptions

import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.models.MemeLock

class RepoConcurrencyException(
    id: MemeId,
    expectedLock: MemeLock,
    actualLock: MemeLock?
) : RepoMemeException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)