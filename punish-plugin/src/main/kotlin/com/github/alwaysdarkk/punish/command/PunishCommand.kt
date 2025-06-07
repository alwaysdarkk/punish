package com.github.alwaysdarkk.punish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Syntax
import com.github.alwaysdarkk.punish.api.PUNISH_PLAYER_KEY
import com.github.alwaysdarkk.punish.view.PunishView
import me.saiintbrisson.minecraft.ViewFrame
import org.bukkit.entity.Player

@CommandAlias("punir")
class PunishCommand(private val viewFrame: ViewFrame) : BaseCommand() {

    @Default
    @CommandPermission("punish.use")
    @Syntax("<jogador>")
    fun execute(player: Player, playerName: String) {
        viewFrame.open(PunishView::class.java, player, mapOf(PUNISH_PLAYER_KEY to playerName))
    }
}