pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8"
}

stonecutter {
    create(rootProject) {
        versions(
            "1.20.1-fabric" to "1.20.1",
            "1.21.1-fabric" to "1.21.1",
            "1.21.4-fabric" to "1.21.4",
            "1.21.11-fabric" to "1.21.11",
            "26.1.2-fabric" to "26.1.2",
            "26.2-fabric" to "26.2",
            "26.3-fabric" to "26.3",
            "1.21.1-neoforge" to "1.21.1",
            "1.21.4-neoforge" to "1.21.4",
            "1.21.11-neoforge" to "1.21.11",
            "26.1.2-neoforge" to "26.1.2",
            "26.2-neoforge" to "26.2",
            "26.3-neoforge" to "26.3",
            "1.20.1-forge" to "1.20.1",
            "1.21.1-forge" to "1.21.1",
            "1.21.4-forge" to "1.21.4",
            "1.21.11-forge" to "1.21.11",
            "26.1.2-forge" to "26.1.2",
            "26.2-forge" to "26.2",
            "26.3-forge" to "26.3"
        )
        vcsVersion = "1.20.1-fabric"
    }
}

rootProject.name = "BEBPM"
