package ru.otus.otuskotlin.app.ktor.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
}

fun Application.installCallLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/v1") }
    }
}

fun Application.installContentNegotiation() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
}