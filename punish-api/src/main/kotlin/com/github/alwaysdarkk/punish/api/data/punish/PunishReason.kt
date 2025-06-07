package com.github.alwaysdarkk.punish.api.data.punish

import org.bukkit.inventory.ItemStack
import kotlin.time.Duration

data class PunishReason(
    val id: String,
    val name: String,
    val permission: String,
    val punishType: PunishType,
    val slot: Int,
    val itemStack: ItemStack,
    val duration: Duration? = null,
)