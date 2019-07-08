import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("edu.wpi.first.GradleRIO")
}

version = "2020.0.0"



repositories {
    maven { setUrl("https://maven.woke.engineer/") }
}

dependencies {
    compile(project(":core"))
    compile("com.github.Oblarg:command-rewrite-jitpack:1.1.4")

    wpi.deps.wpilibJni().forEach { nativeZip(it) }
    wpi.deps.wpilibDesktopJni().forEach { nativeDesktopZip(it) }
    wpi.deps.wpilibJars().forEach { compile(it) }
}
