plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}