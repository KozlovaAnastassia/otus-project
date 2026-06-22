package config

import io.ktor.server.config.ApplicationConfig
import ru.otus.otuskotlin.repo.inmemory.MemeRepoInMemory
import ru.otus.otuskotlin.repo.postgres.RepoMemeSql
import ru.otus.otuskotlin.repo.postgres.SqlProperties
import ru.otus.otuskotlin.common.repo.IMemeRepo

fun ApplicationConfig.repoConfig(): IMemeRepo {
    val type = propertyOrNull("repo.type")?.getString() ?: "inmemory"

    return when (type) {
        "inmemory" -> {
            MemeRepoInMemory()
        }
        "postgres" -> {
            val host = property("repo.postgres.host").getString()
            val port = property("repo.postgres.port").getString().toInt()
            val database = property("repo.postgres.database").getString()
            val user = property("repo.postgres.user").getString()
            val password = property("repo.postgres.password").getString()
            val schema = propertyOrNull("repo.postgres.schema")?.getString() ?: "public"

            val properties = SqlProperties(
                host = host,
                port = port,
                database = database,
                user = user,
                password = password,
                schema = schema
            )
            RepoMemeSql(properties)
        }
        else -> throw IllegalArgumentException("Unknown repo type: $type")
    }
}