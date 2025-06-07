package com.github.alwaysdarkk.punish.api.util

import com.github.alwaysdarkk.punish.api.kt.withPlaceholders
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(private var itemStack: ItemStack) {

    constructor(material: Material) : this(ItemStack(material))

    init {
        this.itemStack = itemStack.clone()
    }

    fun displayName(newName: String) = modifyItemMeta { displayName = newName }
    fun lore(newLore: List<String>) = modifyItemMeta { lore = newLore }

    fun withPlaceholders(placeholders: Map<String, String>) = modifyItemMeta {
        val newDisplayName = displayName.withPlaceholders(placeholders)
        val newLore = lore.withPlaceholders(placeholders)

        displayName = newDisplayName
        lore = newLore
    }

    fun itemFlags(vararg itemFlags: ItemFlag) = modifyItemMeta { addItemFlags(*itemFlags) }
    fun enchantment(enchantment: Enchantment, level: Int) = apply {
        itemStack.addUnsafeEnchantment(enchantment, level)
    }

    fun lore(vararg newLore: String) = this.lore(newLore.toList())
    fun build() = itemStack

    private fun modifyItemMeta(action: ItemMeta.() -> Unit) = apply {
        val itemMeta = itemStack.itemMeta ?: return@apply
        itemMeta.apply(action)

        itemStack.itemMeta = itemMeta
    }
}