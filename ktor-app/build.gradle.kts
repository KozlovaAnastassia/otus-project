plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.7"
}

group = "ru.otus.otuskotlin"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("ru.otus.otuskotlin.app.ktor.ApplicationKt")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-jackson:2.3.7")
    implementation("io.ktor:ktor-server-call-logging:2.3.7")

    // Логирование
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // модули
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":mappers"))
    implementation(project(":biz"))
    implementation(project(":repo-inmemory"))

    // Kotlinx datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")

    // Тесты
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    implementation("io.ktor:ktor-server-config-yaml:2.3.7")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

ktor {
    fatJar {
        archiveFileName.set("ktor-app.jar")
    }
}