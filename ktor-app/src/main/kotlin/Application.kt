package ru.otus.otuskotlin.app.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.otus.otuskotlin.app.ktor.plugins.configureSerialization
import ru.otus.otuskotlin.app.ktor.v1.configureRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}