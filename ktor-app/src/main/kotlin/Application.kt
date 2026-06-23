package ru.otus.otuskotlin.app.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import ru.otus.otuskotlin.app.ktor.plugins.configureSerialization
import ru.otus.otuskotlin.app.ktor.v1.configureRouting
import ru.otus.otuskotlin.app.ktor.config.MemeAppSettings
import java.sql.DriverManager

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    initDatabase()

    val appSettings = MemeAppSettings()

    configureSerialization()
    configureRouting(appSettings)
}

fun Application.initDatabase() {
    try {
        val url = "jdbc:postgresql://localhost:5432/postgres"
        val user = "postgres"
        val password = "postgres"

        Class.forName("org.postgresql.Driver")

        val connection = DriverManager.getConnection(url, user, password)
        val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))

        val liquibase = Liquibase(
            "db/changelog/db.changelog-master.yaml",
            ClassLoaderResourceAccessor(),
            database
        )
        liquibase.update("")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
