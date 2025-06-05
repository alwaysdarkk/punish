package com.github.alwaysdarkk.punish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Syntax
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.repository.PunishRepository
import org.bukkit.command.CommandSender

@CommandAlias("revogar")
class RevokeCommand : BaseCommand() {

    @Default
    @CommandPermission("punish.revoke")
    @Syntax("<id>")
    fun executeCommand(sender: CommandSender, id: String) {
        val punish = PunishCache.find(id)
            ?: return sender.sendMessage("§cEsta punição não foi encontrada.")

        PunishCache.delete(punish)
        PunishRepository.delete(punish)

        sender.sendMessage("§ePunição §f${id} §erevogada com sucesso.")
    }
}