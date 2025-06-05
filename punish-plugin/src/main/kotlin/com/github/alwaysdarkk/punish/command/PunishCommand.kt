package com.github.alwaysdarkk.punish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.alwaysdarkk.punish.api.cache.PunishReasonCache
import com.github.alwaysdarkk.punish.api.handler.PunishHandler
import org.bukkit.command.CommandSender

@CommandAlias("punir")
class PunishCommand : BaseCommand() {

    @Default
    @CommandPermission("punish.use")
    @Syntax("<jogador> <id> [prova]")
    fun execute(sender: CommandSender, playerName: String, reasonId: String, @Optional evidence: String?) {
        val reason = PunishReasonCache.find(reasonId)
            ?: return sender.sendMessage("§cEste motivo não foi encontrado.")

        if (evidence == null && !sender.hasPermission("punish.evidence.bypass")) {
            return sender.sendMessage("§cVocê não tem permissão para isto.")
        }

        PunishHandler.handle(playerName, sender.name, reason, evidence)
    }
}