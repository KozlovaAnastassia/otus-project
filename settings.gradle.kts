rootProject.name = "otus-project"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":api")
include(":common")
include(":mappers")
include(":jakson")
include(":ktor-app")
include(":kafka-app")
include(":libs")
include(":biz")
include(":repo-inmemory")
include(":repo-postgres")