package ru.otus.otuskotlin.repo.postgres

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "postgres",
    val database: String = "postgres",
    val schema: String = "public",
    val table: String = "memes",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}