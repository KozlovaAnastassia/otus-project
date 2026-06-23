package ru.otus.otuskotlin.common.repo

import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeId
import ru.otus.otuskotlin.common.models.MemeFilter

interface IMemeRepo {
    suspend fun create(meme: Meme): MemeRepoResult
    suspend fun read(id: MemeId): MemeRepoResult
    suspend fun update(meme: Meme): MemeRepoResult
    suspend fun delete(id: MemeId): MemeRepoResult
    suspend fun search(filter: MemeFilter): MemeRepoResult

    companion object {
        val NONE = object : IMemeRepo {
            override suspend fun create(meme: Meme): MemeRepoResult {
                throw NotImplementedError("Must not be used")
            }
            override suspend fun read(id: MemeId): MemeRepoResult {
                throw NotImplementedError("Must not be used")
            }
            override suspend fun update(meme: Meme): MemeRepoResult {
                throw NotImplementedError("Must not be used")
            }
            override suspend fun delete(id: MemeId): MemeRepoResult {
                throw NotImplementedError("Must not be used")
            }
            override suspend fun search(filter: MemeFilter): MemeRepoResult {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}