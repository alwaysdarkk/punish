package com.github.alwaysdarkk.punish

import co.aikar.commands.BukkitCommandManager
import com.github.alwaysdarkk.punish.api.adapter.MessageAdapter
import com.github.alwaysdarkk.punish.api.adapter.PunishReasonAdapter
import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.kt.setupConfigLoader
import com.github.alwaysdarkk.punish.api.repository.PunishRepository
import com.github.alwaysdarkk.punish.api.repository.provider.RepositoryProvider
import com.github.alwaysdarkk.punish.api.repository.provider.RepositoryProvider.coroutineScope
import com.github.alwaysdarkk.punish.command.PunishCommand
import com.github.alwaysdarkk.punish.command.RevokeCommand
import com.github.alwaysdarkk.punish.listener.PlayerChatListener
import com.github.alwaysdarkk.punish.listener.PlayerCommandListener
import com.github.alwaysdarkk.punish.listener.PlayerPreLoginListener
import com.github.alwaysdarkk.punish.runnable.PunishExpireRunnable
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PunishPlugin : JavaPlugin() {

    private val reasonsConfig by lazy { setupConfigLoader("reasons.yml").load() }
    private val messagesConfig by lazy { setupConfigLoader("messages.yml").load() }
    private val mainConfig by lazy { setupConfigLoader("config.yml").load() }

    override fun onEnable() {
        PunishReasonAdapter.setup(reasonsConfig)
        MessageAdapter.setup(messagesConfig)

        RepositoryProvider.setup(this, mainConfig)

        coroutineScope.launch {
            PunishRepository.findAll().forEach(PunishCache::insert)
        }

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(PlayerPreLoginListener(), this)
        pluginManager.registerEvents(PlayerChatListener(), this)
        pluginManager.registerEvents(PlayerCommandListener(), this)

        val commandManager = BukkitCommandManager(this)
        commandManager.registerCommand(PunishCommand())
        commandManager.registerCommand(RevokeCommand())

        PunishExpireRunnable(this)
    }
}