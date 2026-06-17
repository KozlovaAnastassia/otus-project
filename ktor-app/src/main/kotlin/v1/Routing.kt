package ru.otus.otuskotlin.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/v1/meme") {
            post("/create") {
                call.createMeme()
            }
            post("/read") {
                call.readMeme()
            }
            post("/update") {
                call.updateMeme()
            }
            post("/delete") {
                call.deleteMeme()
            }
            post("/search") {
                call.searchMeme()
            }
        }
    }
}