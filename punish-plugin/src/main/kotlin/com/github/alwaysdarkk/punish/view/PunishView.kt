package com.github.alwaysdarkk.punish.view

import com.github.alwaysdarkk.punish.PunishPlugin
import com.github.alwaysdarkk.punish.api.PUNISH_EVIDENCE_KEY
import com.github.alwaysdarkk.punish.api.PUNISH_PLAYER_KEY
import com.github.alwaysdarkk.punish.api.PUNISH_REASON_KEY
import com.github.alwaysdarkk.punish.api.cache.InventoryIconCache
import com.github.alwaysdarkk.punish.api.cache.PunishReasonCache
import com.github.alwaysdarkk.punish.api.data.icon.impl.DynamicInventoryIcon
import com.github.alwaysdarkk.punish.api.data.icon.impl.StaticInventoryIcon
import com.github.alwaysdarkk.punish.api.data.punish.PunishReason
import com.github.alwaysdarkk.punish.api.handler.PunishHandler
import com.github.alwaysdarkk.punish.api.kt.formatTime
import com.github.alwaysdarkk.punish.api.kt.withPlaceholders
import com.github.alwaysdarkk.punish.api.util.ItemBuilder
import com.github.alwaysdarkk.punish.listener.conversation.ConversationCancellerListener
import com.github.alwaysdarkk.punish.prompt.SelectEvidencePrompt
import me.saiintbrisson.minecraft.View
import me.saiintbrisson.minecraft.ViewContext
import me.saiintbrisson.minecraft.ViewItemHandler
import me.saiintbrisson.minecraft.ViewSlotClickContext
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

class PunishView(private val plugin: PunishPlugin) : View(6, "Punir") {

    init {
        isCancelOnClick = true
        isCancelOnDrag = true
        isCancelOnDrop = true
        isCancelOnPickup = true
    }

    override fun onRender(context: ViewContext) {
        InventoryIconCache.findStaticIcons().forEach {
            context.slot(it.slot)
                .onRender(buildStaticIcon(it))
                .onClick(handleStaticClick(it))
        }

        PunishReasonCache.findAll().forEach {
            context.slot(it.slot)
                .onRender(buildReasonIcon(it))
                .onClick(handleReasonClick(it))
        }
    }

    private fun buildStaticIcon(icon: StaticInventoryIcon): ViewItemHandler {
        return ViewItemHandler {
            val player = it.get<String>(PUNISH_PLAYER_KEY) ?: error("player is null")
            val reason = it.get<PunishReason>(PUNISH_REASON_KEY)

            val evidence = it.get<String>(PUNISH_EVIDENCE_KEY) ?: "Nenhuma"
            val placeholders = mapOf(
                "player" to player,
                "reason" to (reason?.name ?: "Nenhum"),
                "evidence" to evidence
            )

            val itemStack = ItemBuilder(icon.itemStack)
                .withPlaceholders(placeholders)
                .build()

            it.setItem(itemStack)
        }
    }

    private fun handleStaticClick(icon: StaticInventoryIcon): Consumer<ViewSlotClickContext> {
        return Consumer {
            it.close()

            when (icon.id) {
                "evidence-icon" -> handleEvidenceClick(it)
                "confirm-icon" -> handleConfirmClick(it)
            }
        }
    }

    private fun handleEvidenceClick(context: ViewSlotClickContext) {
        val player = context.player
        val punishPlayer = context.get<String>(PUNISH_PLAYER_KEY)
            ?: return player.sendMessage("§cVocê precisa informar um jogador para punir.")

        val reason = context.get<PunishReason>(PUNISH_REASON_KEY)
        val conversation = ConversationFactory(plugin)
            .addConversationAbandonedListener(ConversationCancellerListener())
            .withFirstPrompt(SelectEvidencePrompt(punishPlayer, reason, plugin))
            .withTimeout(30)
            .withLocalEcho(false)
            .withEscapeSequence("cancelar")
            .buildConversation(player)

        player.beginConversation(conversation)
    }

    private fun handleConfirmClick(context: ViewSlotClickContext) {
        val player = context.player
        val reason = context.get<PunishReason>(PUNISH_REASON_KEY)
            ?: return player.sendMessage("§cVocê precisa informar um motivo.")

        val evidence = context.get<String>(PUNISH_EVIDENCE_KEY)
        if (evidence == null && !player.hasPermission("punish.evidence.bypass")) {
            return player.sendMessage("§cVocê precisa informar uma prova.")
        }

        val punishPlayer = context.get<String>(PUNISH_PLAYER_KEY)
            ?: return player.sendMessage("§cVocê precisa informar um jogador para punir.")

        PunishHandler.handle(punishPlayer, player.name, reason, evidence)
    }

    private fun buildReasonIcon(reason: PunishReason): ViewItemHandler {
        return ViewItemHandler {
            val punishPlayer = it.get<String>(PUNISH_PLAYER_KEY) ?: error("player is null")
            val duration = reason.duration?.inWholeMilliseconds?.formatTime ?: "Eterna"

            val placeholders = mapOf(
                "name" to reason.name,
                "player" to punishPlayer,
                "type" to reason.punishType.displayName,
                "duration" to duration
            )

            val currentReason = it.get<PunishReason>(PUNISH_REASON_KEY)
            if (currentReason != null && currentReason == reason) {
                val itemStack = currentReasonIcon(reason, placeholders) ?: error("current reason icon is null")
                return@ViewItemHandler it.setItem(itemStack)
            }

            val player = it.player
            if (!player.hasPermission(reason.permission)) {
                val itemStack = withoutPermissionIcon(reason, placeholders) ?: error("without permission icon is null")
                return@ViewItemHandler it.setItem(itemStack)
            }

            val itemStack = withPermissionIcon(reason, placeholders) ?: error("with permission icon is null")
            it.setItem(itemStack)
        }
    }

    private fun handleReasonClick(reason: PunishReason): Consumer<ViewSlotClickContext> {
        return Consumer {
            val player = it.player
            if (!player.hasPermission(reason.permission)) {
                return@Consumer player.sendMessage("§cVocê não possui permissão para isto.")
            }

            val currentReason = it.get<PunishReason>(PUNISH_REASON_KEY)
            if (currentReason != null && currentReason == reason) {
                return@Consumer player.sendMessage("§cEste já é o motivo atual.")
            }

            it.set(PUNISH_REASON_KEY, reason)
            it.update()
        }
    }

    private fun currentReasonIcon(reason: PunishReason, placeholders: Map<String, String>): ItemStack? {
        val icon = InventoryIconCache.find("current-reason-icon") as? DynamicInventoryIcon ?: return null
        val displayName = icon.displayName.withPlaceholders(placeholders)

        val lore = icon.lore.withPlaceholders(placeholders)
        return ItemBuilder(reason.itemStack)
            .displayName(displayName)
            .lore(lore)
            .build()
    }

    private fun withoutPermissionIcon(reason: PunishReason, placeholders: Map<String, String>): ItemStack? {
        val icon = InventoryIconCache.find("without-permission-icon") as? DynamicInventoryIcon ?: return null
        val displayName = icon.displayName.withPlaceholders(placeholders)

        val lore = icon.lore.withPlaceholders(placeholders)
        return ItemBuilder(reason.itemStack)
            .displayName(displayName)
            .lore(lore)
            .build()
    }

    private fun withPermissionIcon(reason: PunishReason, placeholders: Map<String, String>): ItemStack? {
        val icon = InventoryIconCache.find("with-permission-icon") as? DynamicInventoryIcon ?: return null
        val displayName = icon.displayName.withPlaceholders(placeholders)

        val lore = icon.lore.withPlaceholders(placeholders)
        return ItemBuilder(reason.itemStack)
            .displayName(displayName)
            .lore(lore)
            .build()
    }
}