package ru.otus.otuskotlin.biz

import ru.otus.otuskotlin.biz.general.initStatus
import ru.otus.otuskotlin.biz.general.operation
import ru.otus.otuskotlin.biz.general.prepareResult
import ru.otus.otuskotlin.biz.stubs.*
import ru.otus.otuskotlin.biz.validation.*
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.Meme
import ru.otus.otuskotlin.common.models.MemeCommand
import ru.otus.otuskotlin.common.models.MemeId
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.cor.rootChain
import ru.otus.otuskotlin.cor.worker

class MemeProcessor {
    suspend fun exec(ctx: MemeContext) = businessChain.exec(ctx)

    private val businessChain = rootChain<MemeContext> {
        initStatus("Инициализация статуса")

        // CREATE
        operation("Создание мема", MemeCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешного создания")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в memeValidating") {
                    memeValidating = memeRequest.deepCopy()
                }
                worker("Очистка id") {
                    memeValidating.id = MemeId.NONE
                }
                worker("Очистка заголовка") {
                    memeValidating.title = memeValidating.title.trim()
                }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                finishMemeValidation("Завершение проверок")
            }
            prepareResult("Подготовка ответа")
        }

        // READ
        operation("Получить мем", MemeCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешного чтения")
                stubValidationBadId("Имитация ошибки валидации id")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в memeValidating") {
                    memeValidating = memeRequest.deepCopy()
                }
                worker("Очистка id") {
                    memeValidating.id = MemeId(memeValidating.id.asString().trim())
                }
                validateIdNotEmpty("Проверка на непустой id")
                finishMemeValidation("Успешное завершение валидации")
            }
            worker("Подготовка ответа для READ") {
                memeResponse = Meme(
                    id = memeValidated.id,
                    title = "Stub мем ${memeValidated.id}",
                    tags = listOf("stub", "read"),
                    image = "/uploads/stub.jpg",
                    createdAt = Clock.System.now()
                )
            }
            prepareResult("Подготовка ответа")
        }

        // UPDATE
        operation("Изменить мем", MemeCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешного обновления")
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в memeValidating") {
                    memeValidating = memeRequest.deepCopy()
                }
                worker("Очистка id") {
                    memeValidating.id = MemeId(memeValidating.id.asString().trim())
                }
                worker("Очистка заголовка") {
                    memeValidating.title = memeValidating.title.trim()
                }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                finishMemeValidation("Успешное завершение валидации")
            }
            prepareResult("Подготовка ответа")
        }

        // DELETE
        operation("Удалить мем", MemeCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешного удаления")
                stubValidationBadId("Имитация ошибки валидации id")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в memeValidating") {
                    memeValidating = memeRequest.deepCopy()
                }
                worker("Очистка id") {
                    memeValidating.id = MemeId(memeValidating.id.asString().trim())
                }
                validateIdNotEmpty("Проверка на непустой id")
                finishMemeValidation("Успешное завершение валидации")
            }

            worker("Подготовка ответа для DELETE") {
                memeResponse = Meme(
                    id = memeValidated.id,
                    title = "Удалённый мем ${memeValidated.id}"
                )
            }
            prepareResult("Подготовка ответа")
        }

        // SEARCH
        operation("Поиск мемов", MemeCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Успешный поиск")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в memeFilterValidating") {
                    memeFilterValidating = memeFilterRequest.deepCopy()
                }
                validateSearchStringLength("Валидация длины строки поиска")
                finishMemeFilterValidation("Успешное завершение валидации")
            }
            prepareResult("Подготовка ответа")
        }
    }.build()
}