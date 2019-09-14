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
    compile(project(":wpi"))
    wpi.deps.vendor.java().forEach { compile(it) }
}

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            groupId = "frc.team4069.saturn.lib"
            artifactId = "vendorREV"
            version = version

            from(components["java"])
        }
    }
}
