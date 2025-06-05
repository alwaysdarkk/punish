package com.github.alwaysdarkk.punish.api.kt

import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File

fun JavaPlugin.setupConfigLoader(fileName: String): YamlConfigurationLoader {
    if (!dataFolder.exists()) {
        dataFolder.mkdirs()
    }

    val file = File(dataFolder, fileName)
    if (!file.exists()) {
        saveResource(fileName, false)
    }

    return YamlConfigurationLoader.builder().path(file.toPath()).build()
}