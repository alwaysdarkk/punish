package com.github.alwaysdarkk.punish.api.kt

import com.github.alwaysdarkk.punish.api.util.CustomSkull
import com.github.alwaysdarkk.punish.api.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
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

fun ConfigurationNode.adaptItemStack(): ItemStack? {
    val rawMaterial = node("material").string ?: return null
    val itemBuilder = if (rawMaterial.contains(":")) {
        val (rawId, rawData) = rawMaterial.split(":")
        val materialId = rawId.toIntOrNull() ?: return null

        val materialData = rawData.toShortOrNull() ?: return null
        ItemBuilder(ItemStack(Material.getMaterial(materialId), 1, materialData))
    } else {
        ItemBuilder(CustomSkull.skullFromUrl(rawMaterial))
    }

    node("display-name").string?.let { itemBuilder.displayName(it) }
    node("lore").getList(String::class.java)?.let { itemBuilder.lore(it) }

    node("item-flags").getList(String::class.java)
        ?.map(ItemFlag::valueOf)?.toTypedArray()?.let { itemBuilder.itemFlags(*it) }

    node("enchantments").getList(String::class.java)?.forEach {
        val (name, rawLevel) = it.split(":")
        val enchantment = Enchantment.getByName(name) ?: return null

        val level = rawLevel.toIntOrNull() ?: return null
        itemBuilder.enchantment(enchantment, level)
    }

    return itemBuilder.build()
}