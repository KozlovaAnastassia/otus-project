package ru.otus.otuskotlin.app.ktor.config

import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.repo.inmemory.MemeRepoInMemory

class MemeAppSettings {
    val repo: IMemeRepo = MemeRepoInMemory()
    val logger: String = "logback"
}