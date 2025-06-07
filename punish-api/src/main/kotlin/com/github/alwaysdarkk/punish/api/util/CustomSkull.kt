package com.github.alwaysdarkk.punish.api.util

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.net.URI
import java.net.URISyntaxException
import java.util.*

object CustomSkull {

    private val skullItemStack = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())

    fun skullFromName(playerName: String): ItemStack = Bukkit.getUnsafe()
        .modifyItemStack(skullItemStack.clone(), "{SkullOwner:\"$playerName\"}").clone()

    fun skullFromUrl(skullId: String): ItemStack {
        val url = "http://textures.minecraft.net/texture/$skullId"
        val base64 = urlToBase64(url)

        return skullFromBase64(base64)
    }

    fun skullFromBase64(base64: String): ItemStack {
        val hashAsId = UUID(base64.hashCode().toLong(), base64.hashCode().toLong())
        return Bukkit.getUnsafe()
            .modifyItemStack(
                skullItemStack.clone(),
                "{SkullOwner:{Id:\"$hashAsId\",Properties:{textures:[{Value:\"$base64\"}]}}}"
            )
            .clone()
    }

    private fun urlToBase64(url: String): String {
        return try {
            val uri = URI(url)
            val toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"$uri\"}}}"

            Base64.getEncoder().encodeToString(toEncode.toByteArray())
        } catch (exception: URISyntaxException) {
            throw RuntimeException(exception)
        }
    }
}