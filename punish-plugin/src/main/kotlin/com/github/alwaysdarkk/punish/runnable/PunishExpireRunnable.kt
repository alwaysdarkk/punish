package com.github.alwaysdarkk.punish.runnable

import com.github.alwaysdarkk.punish.api.cache.PunishCache
import com.github.alwaysdarkk.punish.api.repository.PunishRepository
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class PunishExpireRunnable(plugin: Plugin) : Runnable {

    init {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 60L)
    }

    override fun run() {
        val expiredPunishes = PunishCache.findExpiredPunishes()
        if (expiredPunishes.isEmpty()) {
            return
        }

        expiredPunishes.forEach {
            PunishCache.delete(it)
            PunishRepository.delete(it)
        }
    }
}