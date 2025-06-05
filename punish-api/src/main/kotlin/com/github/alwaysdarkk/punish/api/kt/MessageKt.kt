package com.github.alwaysdarkk.punish.api.kt

import org.bukkit.ChatColor

val String.colored: String get() = ChatColor.translateAlternateColorCodes('&', this)

fun List<String>.withPlaceholders(placeholders: Map<String, String>): List<String> = map {
    placeholders.entries.fold(it) { line, (key, value) -> line.replace("{$key}", value) }.colored
}.map { it.ifBlank { " " } }