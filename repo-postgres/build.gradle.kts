plugins {
    kotlin("jvm") version "1.9.20"
}

group = "ru.otus.otuskotlin"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":libs"))

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")

    // PostgreSQL driver
    implementation("org.postgresql:postgresql:42.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // Тесты
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    implementation("org.jetbrains.exposed:exposed-json:0.44.0")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    enabled = false
    useJUnitPlatform()
    environment("DOCKER_HOST", "unix:///var/run/docker.sock")
    environment("TESTCONTAINERS_RYUK_DISABLED", "true")
}