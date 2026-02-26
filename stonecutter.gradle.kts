plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT" apply false
}

stonecutter active "1.20.1"

stonecutter parameters {
    swaps["mod_version"] = "\"${property("mod.version")}\""
    swaps["minecraft"] = "\"${node.metadata.version}\""
}
