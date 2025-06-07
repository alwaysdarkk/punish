package com.github.alwaysdarkk.punish.api.kt

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

private val locale = Locale.forLanguageTag("pt-BR")
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm", locale)

val Long.formatDate: String
    get() = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)

val Long.formatTime: String get() {
    if (this <= 0L) {
        return "0s"
    }

    val days = TimeUnit.MILLISECONDS.toDays(this)
    val hours = TimeUnit.MILLISECONDS.toHours(this) % 24

    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60

    val units = listOf(
        days to "d",
        hours to "h",
        minutes to "m",
        seconds to "s"
    )

    val parts = units.filter { (value, _) -> value > 0 }.map { (value, suffix) -> "$value$suffix" }
    return parts.firstOrNull() ?: "0s".takeIf { parts.size == 1 } ?: parts.joinToString(" ")
}