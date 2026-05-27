import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class BuildPluginMpp : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        val libs = project.extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")

        val jvmTarget = libs.findVersion("jvmTarget").get().requiredVersion

        project.group = project.rootProject.group
        project.version = project.rootProject.version

        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

        kotlin.jvm()

        kotlin.jvmToolchain(jvmTarget.toInt())

        project.dependencies {
            add("commonMainImplementation", libs.findLibrary("kotlin-stdlib-common").get())
            add("commonTestImplementation", libs.findLibrary("kotlin-test-common").get())
            add("commonTestImplementation", libs.findLibrary("kotlin-test-annotations-common").get())
            add("jvmMainImplementation", libs.findLibrary("kotlin-stdlib").get())
            add("jvmTestImplementation", libs.findLibrary("kotlin-test-junit5").get())
            add("jvmTestImplementation", libs.findLibrary("junit-jupiter").get())
        }

        project.tasks.withType(Test::class.java).configureEach {
            useJUnitPlatform()
        }
    }
}
