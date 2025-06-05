package com.github.alwaysdarkk.punish.api.handler

import com.github.alwaysdarkk.punish.api.cache.MessageCache
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.data.Punish
import com.github.alwaysdarkk.punish.api.data.PunishReason
import com.github.alwaysdarkk.punish.api.data.PunishType
import com.github.alwaysdarkk.punish.api.repository.PunishRepository
import org.apache.commons.lang.RandomStringUtils
import org.bukkit.Bukkit

object PunishHandler {

    fun handle(playerName: String, author: String, reason: PunishReason, evidence: String?) {
        val expireDate = reason.duration?.let { System.currentTimeMillis() + it.inWholeMilliseconds }
        val punish = Punish(
            RandomStringUtils.randomAlphanumeric(5),
            playerName,
            author,
            reason.id,
            evidence,
            expireDate
        )

        PunishCache.insert(punish)
        PunishRepository.insert(punish)

        val chatMessage = MessageCache.findChatMessage(punish)
        Bukkit.getOnlinePlayers().forEach { it.sendMessage(chatMessage) }

        if (reason.punishType != PunishType.BAN) {
            return
        }

        val disconnectMessage = MessageCache.findDisconnectMessage(punish)
        Bukkit.getPlayer(playerName)?.kickPlayer(disconnectMessage)
    }
}