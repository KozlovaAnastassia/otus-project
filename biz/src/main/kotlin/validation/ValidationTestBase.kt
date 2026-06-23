package ru.otus.otuskotlin.biz.validation

import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeFilter
import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.common.repo.MemeRepoResult
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk

class TestRepo : IMemeRepo {
    override suspend fun create(meme: Meme): MemeRepoResult = MemeRepoResultOk(meme)
    override suspend fun read(id: MemeId): MemeRepoResult = MemeRepoResultOk(Meme())
    override suspend fun update(meme: Meme): MemeRepoResult = MemeRepoResultOk(meme)
    override suspend fun delete(id: MemeId): MemeRepoResult = MemeRepoResultOk(Meme())
    override suspend fun search(filter: MemeFilter): MemeRepoResult = MemeRepoResultOk(emptyList())
}