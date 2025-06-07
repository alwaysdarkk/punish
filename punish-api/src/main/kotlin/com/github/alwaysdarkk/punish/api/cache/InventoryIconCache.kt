package com.github.alwaysdarkk.punish.api.cache

import com.github.alwaysdarkk.punish.api.data.icon.InventoryIcon
import com.github.alwaysdarkk.punish.api.data.icon.impl.StaticInventoryIcon

object InventoryIconCache {

    private val iconMap = mutableMapOf<String, InventoryIcon>()

    fun insert(icon: InventoryIcon) {
        iconMap[icon.id] = icon
    }

    fun find(id: String): InventoryIcon? = iconMap[id]
    fun findAll(): List<InventoryIcon> = iconMap.values.toList()

    fun findStaticIcons(): List<StaticInventoryIcon> =
        findAll().filterIsInstance<StaticInventoryIcon>()
}