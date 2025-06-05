package com.github.alwaysdarkk.punish.api.adapter

import com.github.alwaysdarkk.punish.api.cache.MessageCache
import org.spongepowered.configurate.CommentedConfigurationNode

object MessageAdapter {

    fun setup(messagesConfig: CommentedConfigurationNode) {
        val messagesNode = messagesConfig.node("messages") ?: error("messages section is null")
        messagesNode.childrenMap().forEach { (key, value) ->
            val messageId = key.toString()
            val message = value.getList(String::class.java) ?: emptyList()

            MessageCache.insert(messageId, message)
        }
    }
}