package ru.otus.otuskotlin.app.ktor.config

import ru.otus.otuskotlin.common.repo.IMemeRepo
import ru.otus.otuskotlin.repo.inmemory.MemeRepoInMemory
import ru.otus.otuskotlin.repo.postgres.RepoMemeSql
import ru.otus.otuskotlin.repo.postgres.SqlProperties
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import java.sql.DriverManager

class MemeAppSettings {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val config = ConfigFactory.load()
    init {
        try {
            val host = config.getString("repo.postgres.host")
            val port = config.getInt("repo.postgres.port")
            val url = "jdbc:postgresql://$host:$port/postgres"

            Class.forName("org.postgresql.Driver")
            val connection = DriverManager.getConnection(url, "postgres", "postgres")
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val repo: IMemeRepo by lazy {
        val repoType = try {
            config.getString("repo.type")
        } catch (e: Exception) {
            log.warn("repo.type not found in config, using inmemory")
            "inmemory"
        }

        when (repoType.lowercase()) {
            "inmemory" -> {
                log.info("Using InMemory repository")
                MemeRepoInMemory()
            }

            "postgres" -> {
                log.info("Using PostgreSQL repository")
                val host = config.getString("repo.postgres.host")
                val port = config.getInt("repo.postgres.port")
                val user = config.getString("repo.postgres.user")
                val password = config.getString("repo.postgres.password")
                val database = if (config.hasPath("repo.postgres.database")) {
                    config.getString("repo.postgres.database")
                } else "postgres"
                val schema = if (config.hasPath("repo.postgres.schema")) {
                    config.getString("repo.postgres.schema")
                } else "public"

                val properties = SqlProperties(
                    host = host,
                    port = port,
                    user = user,
                    password = password,
                    database = database,
                    schema = schema
                )
                RepoMemeSql(properties)
            }

            else -> {
                MemeRepoInMemory()
            }
        }
    }
}