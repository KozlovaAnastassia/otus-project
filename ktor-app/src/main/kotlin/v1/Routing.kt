package ru.otus.otuskotlin.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.app.ktor.config.MemeAppSettings

fun Application.configureRouting(appSettings: MemeAppSettings) {
    routing {
        route("/v1") {
            memeRoutes(appSettings)
        }
    }
}

fun Route.memeRoutes(appSettings: MemeAppSettings) {
    route("/meme") {
        post("/create") {
            call.createMeme(appSettings)
        }
        post("/read") {
            call.readMeme(appSettings)
        }
        post("/update") {
            call.updateMeme(appSettings)
        }
        post("/delete") {
            call.deleteMeme(appSettings)
        }
        post("/search") {
            call.searchMeme(appSettings)
        }
    }
}