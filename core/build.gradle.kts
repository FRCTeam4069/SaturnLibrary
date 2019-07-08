
version = "2020.0.0"

dependencies {
    compile("com.github.FRCTeam4069:Keigen:1.4.0")
    compile(project(":units"))
}

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            groupId = "frc.team4069.saturn.lib"
            artifactId = "core"
            version = version

            from(components["java"])
        }
    }
}
