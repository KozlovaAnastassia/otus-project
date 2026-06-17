plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.otus.otuskotlin"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // Kafka
    implementation("org.apache.kafka:kafka-clients:3.6.0")

    // Kotlinx coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:atomicfu:0.23.1")

    implementation("ch.qos.logback:logback-classic:1.4.11")

    // модули
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":mappers"))
    implementation(project(":jakson"))

    // Kotlinx datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")

    // Тесты
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveFileName.set("kafka-app.jar")
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = "ru.otus.otuskotlin.app.kafka.MainKt"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}