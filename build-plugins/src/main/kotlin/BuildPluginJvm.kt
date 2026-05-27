import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

@Suppress("unused")
class BuildPluginJvm : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        val libs = project.extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")

        val jvmTarget = libs.findVersion("jvmTarget").get().requiredVersion

        project.group = project.rootProject.group
        project.version = project.rootProject.version

        project.repositories {
            mavenCentral()
        }

        project.extensions.configure(KotlinJvmProjectExtension::class.java) {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(jvmTarget))
            }
        }

        project.dependencies {
            add("implementation", libs.findLibrary("kotlin-stdlib").get())
            add("testImplementation", libs.findLibrary("kotlin-test-junit5").get())
            add("testImplementation", libs.findLibrary("junit-jupiter").get())
        }

        project.tasks.withType(Test::class.java).configureEach {
            useJUnitPlatform()
        }
    }
}
