package com.github.alwaysdarkk.punish.api.data.icon.impl

import com.github.alwaysdarkk.punish.api.data.icon.InventoryIcon
import org.bukkit.inventory.ItemStack

class StaticInventoryIcon(
    override val id: String,
    val slot: Int,
    val itemStack: ItemStack
) : InventoryIcon()