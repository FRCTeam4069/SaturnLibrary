import edu.wpi.first.toolchain.NativePlatforms
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "2020.0.0"

plugins {
    kotlin("jvm") version "1.3.41" apply false
    id("edu.wpi.first.GradleRIO") version "2019.4.1" apply false
    maven
    `maven-publish`
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("maven")
        plugin("maven-publish")
    }

    tasks {
        withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs += "-XXLanguage:+InlineClasses"
            }
        }
    }
    repositories {
        jcenter()
        maven("https://jitpack.io")
    }

    dependencies {
        // Kotlin Standard Library and Coroutines
        "compile"(kotlin("stdlib"))
        "compile"("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.2.1")
        "testCompile"("junit:junit:4.12")
    }
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "5.0"
}
