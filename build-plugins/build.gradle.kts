plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("buildPluginJvm") {
            id = "ru.otus.jvm-conventions"
            implementationClass = "BuildPluginJvm"
        }
        register("buildPluginMpp") {
            id = "ru.otus.multiplatform-conventions"
            implementationClass = "BuildPluginMpp"
        }
    }
}
