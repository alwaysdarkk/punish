package com.github.alwaysdarkk.punish.api.adapter

import com.github.alwaysdarkk.punish.api.cache.PunishReasonCache
import com.github.alwaysdarkk.punish.api.data.PunishReason
import com.github.alwaysdarkk.punish.api.data.PunishType
import org.spongepowered.configurate.CommentedConfigurationNode
import kotlin.time.Duration.Companion.minutes

object PunishReasonAdapter {

    fun setup(reasonsConfig: CommentedConfigurationNode) {
        val reasonsNode = reasonsConfig.node("reasons")
        reasonsNode.childrenMap().values.map(this::adapt).forEach(PunishReasonCache::insert)
    }

    private fun adapt(node: CommentedConfigurationNode): PunishReason {
        val id = node.key().toString()
        val name = node.node("name").string ?: error("name is null")

        val permission = node.node("permission").string ?: error("permission is null")
        val punishType = node.node("type").string ?: error("punish type is null")

        val duration = node.node("duration").let {
            if (it.virtual() || it.string == null) null else it.int.minutes
        }

        return PunishReason(
            id,
            name,
            permission,
            PunishType.valueOf(punishType.uppercase()),
            duration
        )
    }
}