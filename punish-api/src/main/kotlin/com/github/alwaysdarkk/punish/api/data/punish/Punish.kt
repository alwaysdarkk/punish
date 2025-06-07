package com.github.alwaysdarkk.punish.api.data.punish

import com.github.alwaysdarkk.punish.api.cache.PunishReasonCache
import com.github.alwaysdarkk.punish.api.kt.formatDate

data class Punish(
    val id: String,
    val playerName: String,
    val author: String,
    val reasonId: String,
    val evidence: String? = null,
    val expireDate: Long? = null
) {
    val placeholderMap = mapOf(
        "id" to id,
        "player" to playerName,
        "author" to author,
        "reason" to reason.name,
        "evidence" to displayEvidence,
        "expire-date" to (expireDate?.formatDate ?: "Nunca")
    )

    val reason get() = PunishReasonCache.find(reasonId) ?: error("reason is null")
    val displayEvidence get() = evidence ?: "Nenhuma"

    val isTemporary get() = expireDate != null
    val isExpired get() = expireDate?.let { System.currentTimeMillis() >= it } ?: false
}