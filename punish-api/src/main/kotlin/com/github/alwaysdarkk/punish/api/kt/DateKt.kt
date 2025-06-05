package com.github.alwaysdarkk.punish.api.kt

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private val locale = Locale.forLanguageTag("pt-BR")
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm", locale)

val Long.formatDate: String
    get() = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)