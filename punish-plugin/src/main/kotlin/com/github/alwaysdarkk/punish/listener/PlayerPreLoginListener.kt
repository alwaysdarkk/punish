package com.github.alwaysdarkk.punish.listener

import com.github.alwaysdarkk.punish.api.cache.MessageCache
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.data.PunishType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class PlayerPreLoginListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: AsyncPlayerPreLoginEvent) {
        val playerName = event.name
        val punish = PunishCache.find(playerName, PunishType.BAN) ?: return

        if (punish.isExpired) {
            return
        }

        val disconnectMessage = MessageCache.findDisconnectMessage(punish)
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, disconnectMessage)
    }
}