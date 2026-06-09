plugins {
    kotlin("jvm") version "1.9.20"
}

group = "ru.otus.otuskotlin"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":api"))
    implementation(project(":common"))

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
    }
}