plugins {
    kotlin("jvm") version "1.9.20"
}

group = "ru.otus.otuskotlin"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // модули
    implementation(project(":common"))
    implementation(project(":mappers"))
    implementation(project(":libs"))

    // Kotlinx datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // Логирование
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // Тесты
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}