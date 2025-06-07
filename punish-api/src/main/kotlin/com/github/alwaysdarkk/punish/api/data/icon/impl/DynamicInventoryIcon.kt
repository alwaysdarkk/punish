package com.github.alwaysdarkk.punish.api.data.icon.impl

import com.github.alwaysdarkk.punish.api.data.icon.InventoryIcon

class DynamicInventoryIcon(
    override val id: String,
    val displayName: String,
    val lore: List<String>
) : InventoryIcon()