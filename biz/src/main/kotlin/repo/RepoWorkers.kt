package ru.otus.otuskotlin.biz.repo

import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.common.repo.MemeRepoResultErr
import ru.otus.otuskotlin.common.repo.MemeRepoResultErrWithData
import ru.otus.otuskotlin.common.repo.MemeRepoResultOk
import ru.otus.otuskotlin.cor.ICorChainDsl
import ru.otus.otuskotlin.cor.worker

fun ICorChainDsl<MemeContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Сохранение мема в БД"
    on { state == MemeState.RUNNING }
    handle {
        when (val result = repo.create(memeValidated)) {
            is MemeRepoResultOk -> {
                memeValidated = result.data.first()
                memeResponse = result.data.first()
                state = MemeState.FINISHING
            }
            is MemeRepoResultErr -> {
                errors.addAll(result.errors)
                state = MemeState.FAILING
            }
            is MemeRepoResultErrWithData -> {
                errors.addAll(result.errors)
                memeResponse = result.data.first()
                state = MemeState.FAILING
            }
        }
    }
}

fun ICorChainDsl<MemeContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение мема из БД по ID"
    on { state == MemeState.RUNNING }
    handle {
        when (val result = repo.read(memeValidated.id)) {
            is MemeRepoResultOk -> {
                memeResponse = result.data.first()
                state = MemeState.FINISHING
            }
            is MemeRepoResultErr -> {
                errors.addAll(result.errors)
                state = MemeState.FAILING
            }
            is MemeRepoResultErrWithData -> {
                errors.addAll(result.errors)
                memeResponse = result.data.first()
                state = MemeState.FAILING
            }
        }
    }
}

fun ICorChainDsl<MemeContext>.repoUpdate(title: String) = worker {
    this.title = title
    description = "Обновление мема в БД"
    on { state == MemeState.RUNNING }
    handle {
        when (val result = repo.update(memeValidated)) {
            is MemeRepoResultOk -> {
                memeValidated = result.data.first()
                memeResponse = result.data.first()
                state = MemeState.FINISHING
            }
            is MemeRepoResultErr -> {
                errors.addAll(result.errors)
                state = MemeState.FAILING
            }
            is MemeRepoResultErrWithData -> {
                errors.addAll(result.errors)
                memeValidated = result.data.first()
                memeResponse = result.data.first()
                state = MemeState.FAILING
            }
        }
    }
}

fun ICorChainDsl<MemeContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление мема из БД"
    on { state == MemeState.RUNNING }
    handle {
        when (val result = repo.delete(memeValidated.id)) {
            is MemeRepoResultOk -> {
                memeResponse = result.data.first()
                state = MemeState.FINISHING
            }
            is MemeRepoResultErr -> {
                errors.addAll(result.errors)
                state = MemeState.FAILING
            }
            is MemeRepoResultErrWithData -> {
                errors.addAll(result.errors)
                memeResponse = result.data.first()
                state = MemeState.FAILING
            }
        }
    }
}

fun ICorChainDsl<MemeContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск мемов в БД по фильтру"
    on { state == MemeState.RUNNING }
    handle {
        when (val result = repo.search(memeFilterValidated)) {
            is MemeRepoResultOk -> {
                memesResponse = result.data.toMutableList()
                state = MemeState.FINISHING
            }
            is MemeRepoResultErr -> {
                errors.addAll(result.errors)
                state = MemeState.FAILING
            }
            is MemeRepoResultErrWithData -> {
                errors.addAll(result.errors)
                memesResponse = result.data.toMutableList()
                state = MemeState.FAILING
            }
        }
    }
}

fun ICorChainDsl<MemeContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка мема к сохранению в БД"
    on { state == MemeState.RUNNING }
    handle {
        memeValidated = memeValidated.copy(
            createdAt = kotlinx.datetime.Clock.System.now()
        )
    }
}

fun ICorChainDsl<MemeContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Подготовка мема к удалению из БД"
    on { state == MemeState.RUNNING }
    handle {}
}