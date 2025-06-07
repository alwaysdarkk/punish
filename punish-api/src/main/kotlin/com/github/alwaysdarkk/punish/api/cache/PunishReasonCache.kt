package com.github.alwaysdarkk.punish.api.cache

import com.github.alwaysdarkk.punish.api.data.punish.PunishReason

object PunishReasonCache {

    private val reasonMap = mutableMapOf<String, PunishReason>()

    fun insert(reason: PunishReason) {
        reasonMap[reason.id] = reason
    }

    fun find(id: String): PunishReason? = reasonMap[id]
    fun findAll(): List<PunishReason> = reasonMap.values.toList()
}