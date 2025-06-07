package com.github.alwaysdarkk.punish.listener.conversation

import org.bukkit.conversations.ConversationAbandonedEvent
import org.bukkit.conversations.ConversationAbandonedListener
import org.bukkit.conversations.ExactMatchConversationCanceller
import org.bukkit.entity.Player

class ConversationCancellerListener : ConversationAbandonedListener {

    override fun conversationAbandoned(event: ConversationAbandonedEvent) {
        if (event.canceller !is ExactMatchConversationCanceller) {
            return
        }

        val context = event.context
        val player = context.forWhom as Player

        player.sendMessage("§aOperação cancelada com sucesso.")
    }
}