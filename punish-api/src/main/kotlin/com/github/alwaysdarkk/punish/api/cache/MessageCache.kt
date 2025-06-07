package com.github.alwaysdarkk.punish.api.cache

import com.github.alwaysdarkk.punish.api.data.punish.Punish
import com.github.alwaysdarkk.punish.api.kt.withPlaceholders

object MessageCache {

    private val messageMap = mutableMapOf<String, List<String>>()

    fun insert(id: String, message: List<String>) {
        messageMap[id] = message
    }

    fun find(id: String): List<String> = messageMap[id] ?: emptyList()

    fun findDisconnectMessage(punish: Punish): String {
        val messageId = if (punish.isTemporary) "temp-ban-disconnect-message" else "ban-disconnect-message"
        return find(messageId).withPlaceholders(punish.placeholderMap).joinToString("\n")
    }

    fun findMuteMessage(punish: Punish): String {
        val messageId = if (punish.isTemporary) "temp-mute-message" else "mute-message"
        return find(messageId).withPlaceholders(punish.placeholderMap).joinToString("\n")
    }

    fun findChatMessage(punish: Punish): String {
        val punishType = punish.reason.punishType.name.lowercase()
        val messageId = if (punish.isTemporary) "temp-$punishType-chat-message" else "$punishType-chat-message"

        return find(messageId).withPlaceholders(punish.placeholderMap).joinToString("\n")
    }
}