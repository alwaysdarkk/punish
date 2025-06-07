package com.github.alwaysdarkk.punish.api.cache

import com.github.alwaysdarkk.punish.api.data.punish.Punish
import com.github.alwaysdarkk.punish.api.data.punish.PunishType

object PunishCache {

    private val punishMap = mutableMapOf<String, Punish>()

    fun insert(punish: Punish) {
        punishMap[punish.id] = punish
    }

    fun delete(punish: Punish) {
        punishMap.remove(punish.id)
    }

    fun find(id: String): Punish? = punishMap[id]
    fun find(playerName: String, punishType: PunishType): Punish? = findAll().firstOrNull {
        it.playerName == playerName && it.reason.punishType == punishType
    }

    fun findAll(): List<Punish> = punishMap.values.toList()
    fun findExpiredPunishes(): List<Punish> = findAll().filter(Punish::isExpired)
}