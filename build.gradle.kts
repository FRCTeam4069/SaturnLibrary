
import edu.wpi.first.toolchain.NativePlatforms

plugins {
    kotlin("jvm") version "1.3.31"
    id("edu.wpi.first.GradleRIO") version "2019.3.2"
    maven
    `maven-publish`
}

repositories {
    jcenter()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    // Kotlin Standard Library and Coroutines
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.1.0")
    compile("com.github.FRCTeam4069:Keigen:1.1.0")
    testCompile("junit:junit:4.12")

    // WPILib
    wpi.deps.wpilib().forEach { compile(it) }
    wpi.deps.vendor.java().forEach { compile(it) }
    wpi.deps.vendor.jni(NativePlatforms.roborio).forEach { nativeZip(it) }
    wpi.deps.vendor.jni(NativePlatforms.desktop).forEach { nativeDesktopZip(it) }
}

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            groupId = "frc.team4069"
            artifactId = "SaturnLibrary"
            version = "2019.05.25"
            
            from(components["java"])
        }
    } 
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "5.0"
}
