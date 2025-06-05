package com.github.alwaysdarkk.punish.api.data

import kotlin.time.Duration

data class PunishReason(
    val id: String,
    val name: String,
    val permission: String,
    val punishType: PunishType,
    val duration: Duration? = null,
)