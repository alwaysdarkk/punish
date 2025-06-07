package com.github.alwaysdarkk.punish

import co.aikar.commands.BukkitCommandManager
import com.github.alwaysdarkk.punish.api.adapter.InventoryIconAdapter
import com.github.alwaysdarkk.punish.api.adapter.MessageAdapter
import com.github.alwaysdarkk.punish.api.adapter.PunishReasonAdapter
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.kt.setupConfigLoader
import com.github.alwaysdarkk.punish.api.repository.PunishRepository
import com.github.alwaysdarkk.punish.api.repository.provider.RepositoryProvider
import com.github.alwaysdarkk.punish.api.repository.provider.RepositoryProvider.coroutineScope
import com.github.alwaysdarkk.punish.command.PunishCommand
import com.github.alwaysdarkk.punish.command.RevokeCommand
import com.github.alwaysdarkk.punish.listener.player.PlayerChatListener
import com.github.alwaysdarkk.punish.listener.player.PlayerCommandListener
import com.github.alwaysdarkk.punish.listener.player.PlayerPreLoginListener
import com.github.alwaysdarkk.punish.runnable.PunishExpireRunnable
import com.github.alwaysdarkk.punish.view.PunishView
import kotlinx.coroutines.launch
import me.saiintbrisson.minecraft.ViewFrame
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PunishPlugin : JavaPlugin() {

    lateinit var viewFrame: ViewFrame

    override fun onEnable() {
        val reasonsConfig = setupConfigLoader("reasons.yml").load()
        val messagesConfig = setupConfigLoader("messages.yml").load()

        val mainConfig = setupConfigLoader("config.yml").load()
        val iconsConfig = setupConfigLoader("icons.yml").load()

        PunishReasonAdapter.setup(reasonsConfig)
        MessageAdapter.setup(messagesConfig)
        InventoryIconAdapter.setup(iconsConfig)

        RepositoryProvider.setup(this, mainConfig)

        coroutineScope.launch {
            PunishRepository.findAll().forEach(PunishCache::insert)
        }

        viewFrame = ViewFrame.of(this, PunishView(this)).register()

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(PlayerPreLoginListener(), this)
        pluginManager.registerEvents(PlayerChatListener(), this)
        pluginManager.registerEvents(PlayerCommandListener(), this)

        val commandManager = BukkitCommandManager(this)
        commandManager.registerCommand(PunishCommand(viewFrame))
        commandManager.registerCommand(RevokeCommand())

        PunishExpireRunnable(this)
    }
}