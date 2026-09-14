plugins {
    id("net.fabricmc.fabric-loom-remap")
}

version = "${property("mod.version")}-${sc.current.version}"
base.archivesName = property("mod.id") as String

val requiredJava = when {
    sc.current.parsed >= "1.20.6" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    else -> JavaVersion.VERSION_1_8
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    mappings("net.fabricmc:yarn:${property("deps.yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    // Optional at runtime: the config screen is only reached through Mod Menu, which checks
    // that Cloth Config is present first. findProperty keeps versions that have not pinned
    // these yet from breaking configuration for every other version.
    findProperty("deps.cloth_config")?.let {
        modCompileOnly("me.shedaniel.cloth:cloth-config-fabric:$it") { isTransitive = false }
        // me.shedaniel.math.Point, required by Cloth's tooltip API. Compile-time only:
        // Cloth ships this as a nested jar, so it is already present at runtime.
        compileOnly("me.shedaniel.cloth:basic-math:0.6.1")
    }
    findProperty("deps.modmenu")?.let {
        modCompileOnly("com.terraformersmc:modmenu:$it") { isTransitive = false }
    }
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
}

tasks {
    processResources {
        inputs.property("id", project.property("mod.id"))
        inputs.property("name", project.property("mod.name"))
        inputs.property("version", project.property("mod.version"))
        inputs.property("minecraft", project.property("mod.mc_dep"))

        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.property("mod.version"),
            "minecraft" to project.property("mod.mc_dep")
        )

        filesMatching("fabric.mod.json") { expand(props) }

        // 1.21.2+: Tag directories changed from plural to singular (blocks -> block, etc.)
        if (sc.current.parsed >= "1.21.2") {
            doLast {
                val dataDir = destinationDir.resolve("data")
                if (dataDir.exists()) {
                    dataDir.listFiles()?.filter { it.isDirectory }?.forEach { namespaceDir ->
                        val tagsDir = namespaceDir.resolve("tags")
                        if (tagsDir.exists()) {
                            mapOf("blocks" to "block", "items" to "item", "fluids" to "fluid").forEach { (plural, singular) ->
                                val pluralDir = tagsDir.resolve(plural)
                                val singularDir = tagsDir.resolve(singular)
                                if (pluralDir.exists()) {
                                    singularDir.mkdirs()
                                    pluralDir.listFiles()?.forEach { file ->
                                        file.copyTo(singularDir.resolve(file.name), overwrite = true)
                                    }
                                    pluralDir.deleteRecursively()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    withType<JavaCompile> {
        options.release = requiredJava.majorVersion.toInt()
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}" }
        }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile }, remapSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}
