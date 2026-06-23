package ru.otus.otuskotlin.common.repo.exceptions

import ru.otus.otuskotlin.common.models.MemeId

class RepoEmptyLockException(id: MemeId) : RepoMemeException(
    id,
    "Lock is empty in DB"
)