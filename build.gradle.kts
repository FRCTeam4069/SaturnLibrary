import edu.wpi.first.gradlerio.GradleRIOPlugin
import edu.wpi.first.gradlerio.frc.FRCJavaArtifact
import edu.wpi.first.gradlerio.frc.FRCPlugin
import edu.wpi.first.gradlerio.frc.RoboRIO
import edu.wpi.first.gradlerio.wpi.dependencies.WPIVendorDepsExtension
import edu.wpi.first.toolchain.NativePlatforms
import jaci.gradle.deploy.artifact.ArtifactsExtension
import jaci.gradle.deploy.artifact.FileTreeArtifact
import jaci.gradle.deploy.target.TargetsExtension

plugins {
    kotlin("jvm") version "1.3.11"
    id("edu.wpi.first.GradleRIO") version "2019.1.1-rc-1"
    maven
}

repositories {
    jcenter()
    maven { setUrl("http://dl.bintray.com/kyonifer/maven") }
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    // Kotlin Standard Library and Coroutines
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.1.0")
    compile("com.kyonifer:koma-core-ejml:0.12")

    // WPILib
    wpi.deps.wpilib().forEach { compile(it) }
    wpi.deps.vendor.java().forEach { compile(it) }
    wpi.deps.vendor.jni(NativePlatforms.roborio).forEach { nativeZip(it) }
    wpi.deps.vendor.jni(NativePlatforms.desktop).forEach { nativeDesktopZip(it) }
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "5.0"
}
