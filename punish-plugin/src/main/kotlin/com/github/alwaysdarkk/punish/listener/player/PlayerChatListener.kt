package com.github.alwaysdarkk.punish.listener.player

import com.github.alwaysdarkk.punish.api.cache.MessageCache
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.data.punish.PunishType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val punish = PunishCache.find(player.name, PunishType.MUTE) ?: return

        if (punish.isExpired) {
            return
        }

        event.isCancelled = true

        val muteMessage = MessageCache.findMuteMessage(punish)
        player.sendMessage(muteMessage)
    }
}