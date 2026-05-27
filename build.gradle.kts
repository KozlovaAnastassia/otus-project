group = "ru.otus.otuskotlin"
version = "0.0.1"

tasks.register("buildAll") {
    group = "build"
    description = "Build all composite builds"
    dependsOn(gradle.includedBuild("learn").task(":m1l1-first:build"))
    dependsOn(gradle.includedBuild("project").task(":app:build"))
}

tasks.register("cleanAll") {
    group = "build"
    description = "Clean all composite builds"
    dependsOn(gradle.includedBuild("build-plugins").task(":clean"))
    dependsOn(gradle.includedBuild("learn").task(":clean"))
    dependsOn(gradle.includedBuild("project").task(":clean"))
}