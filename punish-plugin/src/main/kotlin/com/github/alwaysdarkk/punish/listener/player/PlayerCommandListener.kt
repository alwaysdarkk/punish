package com.github.alwaysdarkk.punish.listener.player

import com.github.alwaysdarkk.punish.api.cache.MessageCache
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.data.punish.PunishType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class PlayerCommandListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val command = event.message.split(" ")[0].substring(1).lowercase()

        if (command != "g" && command != "l") {
            return
        }

        val punish = PunishCache.find(player.name, PunishType.MUTE) ?: return
        if (punish.isExpired) {
            return
        }

        event.isCancelled = true

        val muteMessage = MessageCache.findMuteMessage(punish)
        player.sendMessage(muteMessage)
    }
}