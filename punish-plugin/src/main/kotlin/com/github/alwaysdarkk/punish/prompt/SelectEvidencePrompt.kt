package com.github.alwaysdarkk.punish.prompt

import com.github.alwaysdarkk.punish.PunishPlugin
import com.github.alwaysdarkk.punish.api.PUNISH_EVIDENCE_KEY
import com.github.alwaysdarkk.punish.api.PUNISH_PLAYER_KEY
import com.github.alwaysdarkk.punish.api.PUNISH_REASON_KEY
import com.github.alwaysdarkk.punish.api.cache.MessageCache
import com.github.alwaysdarkk.punish.api.data.punish.PunishReason
import com.github.alwaysdarkk.punish.api.kt.colored
import com.github.alwaysdarkk.punish.view.PunishView
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class SelectEvidencePrompt(
    private val punishPlayer: String,
    private val reason: PunishReason?,
    private val plugin: PunishPlugin
) : StringPrompt() {
    private val regex = Regex("^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}(/\\S*)?$")

    override fun getPromptText(context: ConversationContext): String {
        return MessageCache.find("select-evidence-message")
            .map(String::colored)
            .joinToString("\n")
    }

    override fun acceptInput(context: ConversationContext, evidence: String): Prompt? {
        val player = context.forWhom as Player
        if (!regex.matches(evidence)) {
            player.sendRawMessage("§cA url informada não é válida.")
            return END_OF_CONVERSATION
        }

        plugin.viewFrame.open(
            PunishView::class.java, player, mapOf(
                PUNISH_PLAYER_KEY to punishPlayer,
                PUNISH_EVIDENCE_KEY to evidence,
                PUNISH_REASON_KEY to reason
            )
        )

        return END_OF_CONVERSATION
    }
}