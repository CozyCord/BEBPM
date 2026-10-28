plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.17.493" apply false
    id("dev.architectury.loom-no-remap") version "1.17.493" apply false
}

stonecutter active "1.20.1-fabric"

stonecutter parameters {
    val loader = node.metadata.project.substringAfterLast('-')
    swaps["mod_version"] = "\"${property("mod.version")}\""
    swaps["minecraft"] = "\"${node.metadata.version}\""
    constants.match(loader, "fabric", "neoforge", "forge")
    constants["cloth"] = node.project.findProperty("deps.cloth_config") != null
}
