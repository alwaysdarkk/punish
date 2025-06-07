package com.github.alwaysdarkk.punish.api.kt

import org.bukkit.ChatColor

val String.colored: String
    get() =
        ChatColor.translateAlternateColorCodes('&', this).ifBlank { " " }

fun String.withPlaceholders(placeholders: Map<String, String>): String {
    return placeholders.entries.fold(this) { line, (key, value) ->
        line.replace("{$key}", value)
    }.colored
}

fun List<String>.withPlaceholders(placeholders: Map<String, String>) =
    map { it.withPlaceholders(placeholders) }