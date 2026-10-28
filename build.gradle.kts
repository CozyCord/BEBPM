import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
}

val loader = sc.current.project.substringAfterLast('-')
val remapped = sc.current.parsed < "26.1"
apply(plugin = if (remapped) "dev.architectury.loom" else "dev.architectury.loom-no-remap")

val loom = the<LoomGradleExtensionAPI>()

version = "${property("mod.version")}-${sc.current.version}"
base.archivesName = "${property("mod.id")}-$loader"

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.6" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    else -> JavaVersion.VERSION_1_8
}
val mixinJava = minOf(requiredJava.majorVersion.toInt(), 21)

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.neoforged.net/releases/")
    mavenCentral()
}

dependencies {
    "minecraft"("com.mojang:minecraft:${sc.current.version}")
    if (remapped) {
        "mappings"(loom.officialMojangMappings())
    }

    when (loader) {
        "fabric" -> {
            if (remapped) "modImplementation"("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
            else "implementation"("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
        }
        "neoforge" -> "neoForge"("net.neoforged:neoforge:${property("deps.neoforge")}")
        "forge" -> "forge"("net.minecraftforge:forge:${sc.current.version}-${property("deps.forge")}")
    }

    val modCompileOnly = if (remapped) "modCompileOnly" else "compileOnly"
    val localRuntime = if (remapped) "modLocalRuntime" else "localRuntime"

    findProperty("deps.cloth_config")?.let {
        modCompileOnly("me.shedaniel.cloth:cloth-config-$loader:$it") { isTransitive = false }
        "compileOnly"("me.shedaniel.cloth:basic-math:0.6.1")
        localRuntime("me.shedaniel.cloth:cloth-config-$loader:$it") { isTransitive = false }
        if (remapped && loader == "fabric") localRuntime("me.shedaniel.cloth:basic-math:0.6.1")
    }
    if (loader == "fabric") {
        findProperty("deps.modmenu")?.let {
            modCompileOnly("com.terraformersmc:modmenu:$it") { isTransitive = false }
            localRuntime("com.terraformersmc:modmenu:$it") { isTransitive = false }
        }
        findProperty("deps.fabric_api")?.let {
            localRuntime("net.fabricmc.fabric-api:fabric-api:$it")
        }
    }
}

loom.runConfigs.all {
    ideConfigGenerated(true)
    runDir = "../../run/${sc.current.project}"
}

if (loader == "forge") {
    loom.forge { mixinConfigs("bebpm.mixins.json") }
    if (remapped) {
        loom.mixin { useLegacyMixinAp.set(true) }
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
}

tasks {
    processResources {
        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.property("mod.version"),
            "minecraft" to project.property("mod.mc_dep"),
            "loader" to project.property("mod.loader_dep"),
            "java" to requiredJava.majorVersion,
            "mixin_java" to mixinJava.toString(),
            "mc_version" to sc.current.version,
            "neoforge_range" to (findProperty("deps.neoforge_range") ?: ""),
            "forge_range" to (findProperty("deps.forge_range") ?: ""),
            "mc_range_toml" to (findProperty("mod.mc_range_toml") ?: ""),
            "pack_format" to (findProperty("mod.pack_format") ?: ""),
            "extra_mixins" to (findProperty("mod.extra_mixins") ?: "")
        )
        props.forEach { (k, v) -> inputs.property(k, v) }

        filesMatching(listOf("fabric.mod.json", "bebpm.mixins.json", "pack.mcmeta",
                             "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) { expand(props) }

        when (loader) {
            "fabric" -> exclude("META-INF/neoforge.mods.toml", "META-INF/mods.toml", "pack.mcmeta")
            "neoforge" -> exclude("fabric.mod.json", "META-INF/mods.toml", "pack.mcmeta")
            "forge" -> exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
        }

        if (sc.current.parsed >= "1.21") {
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
        from(named(if (remapped) "remapJar" else "jar"), named(if (remapped) "remapSourcesJar" else "sourcesJar"))
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}
