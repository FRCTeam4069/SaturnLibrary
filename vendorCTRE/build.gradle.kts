import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            groupId = "frc.team4069.saturn.lib"
            artifactId = "vendorCTRE"
            version = version

            from(components["java"])
        }
    }
}
