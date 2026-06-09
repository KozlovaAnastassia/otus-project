plugins {
    kotlin("jvm") version "1.9.20"
    id("org.openapi.generator") version "7.2.0"
}

group = "ru.otus.otuskotlin"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}