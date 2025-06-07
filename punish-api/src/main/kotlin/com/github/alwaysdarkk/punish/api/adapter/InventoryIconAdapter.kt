package com.github.alwaysdarkk.punish.api.adapter

import com.github.alwaysdarkk.punish.api.cache.InventoryIconCache
import com.github.alwaysdarkk.punish.api.data.icon.InventoryIcon
import com.github.alwaysdarkk.punish.api.data.icon.impl.DynamicInventoryIcon
import com.github.alwaysdarkk.punish.api.data.icon.impl.StaticInventoryIcon
import com.github.alwaysdarkk.punish.api.kt.adaptItemStack
import org.spongepowered.configurate.CommentedConfigurationNode

object InventoryIconAdapter {

    fun setup(iconsConfig: CommentedConfigurationNode) {
        val iconsNode = iconsConfig.node("icons") ?: error("icons section is null")
        iconsNode.childrenMap().values.map(this::adapt).forEach(InventoryIconCache::insert)
    }

    private fun adapt(node: CommentedConfigurationNode): InventoryIcon {
        val id = node.key().toString()
        val slot = node.node("slot").let {
            if (it.virtual() || it.string == null) null else it.int
        }

        if (slot == null) {
            val displayName = node.node("display-name").string ?: error("display-name is null")
            val lore = node.node("lore").getList(String::class.java) ?: error("lore is null")

            return DynamicInventoryIcon(id, displayName, lore)
        }

        val itemStack = node.adaptItemStack() ?: error("itemStack is null")
        return StaticInventoryIcon(id, slot, itemStack)
    }
}