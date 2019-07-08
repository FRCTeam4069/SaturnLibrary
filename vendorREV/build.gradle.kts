import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "2020.0.0"

plugins {
    id("edu.wpi.first.GradleRIO")
}

repositories {
    maven { setUrl("https://maven.woke.engineer/") }
}


dependencies {
    compile(project(":core"))
    compile(project(":wpi"))
    wpi.deps.vendor.java().forEach { compile(it) }
}
