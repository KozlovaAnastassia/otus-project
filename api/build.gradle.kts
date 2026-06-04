plugins {
    kotlin("jvm") version "1.9.20"
    id("org.openapi.generator") version "7.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("kotlin")
    packageName.set("ru.otus.otuskotlin.api.v1")
    modelPackage.set("ru.otus.otuskotlin.api.v1.models")
    inputSpec.set("$projectDir/src/main/resources/openapi/openapi.yaml")
    outputDir.set("$buildDir/generate-resources")

    configOptions.set(mapOf(
        "serializationLibrary" to "jackson"
    ))

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
}

sourceSets {
    main {
        kotlin.srcDir("$buildDir/generate-resources/src/main/kotlin")
    }
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

tasks.clean {
    delete("$buildDir/generate-resources")
}