package com.github.alwaysdarkk.punish.api.repository.provider

import com.github.alwaysdarkk.punish.api.repository.table.PunishTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.bukkit.plugin.Plugin
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.spongepowered.configurate.CommentedConfigurationNode
import java.io.File

object RepositoryProvider {

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun setup(plugin: Plugin, mainConfig: CommentedConfigurationNode) {
        val databaseNode = mainConfig.node("database") ?: error("database section is null")
        val databaseType = databaseNode.node("type").string ?: error("database type is null")

        val databaseTypeNode = databaseNode.node(databaseType) ?: error("database type section is null")
        when (databaseType) {
            "h2" -> handleH2Connection(plugin, databaseTypeNode)
            "mysql" -> handleMysqlConnection(databaseTypeNode)
            else -> error("database type is invalid")
        }

        transaction {
            SchemaUtils.create(PunishTable)
        }
    }

    private fun handleH2Connection(plugin: Plugin, node: CommentedConfigurationNode) {
        val fileName = node.node("fileName").string ?: "database"
        val folder = File(plugin.dataFolder, "database")

        val file = File(folder, fileName)
        Database.connect("jdbc:h2:file:${file.absolutePath};DB_CLOSE_DELAY=-1;")
    }

    private fun handleMysqlConnection(node: CommentedConfigurationNode) {
        val address = node.node("address").string ?: error("mysql address is null")
        val username = node.node("username").string ?: error("mysql username is null")

        val password = node.node("password").string ?: error("mysql password is null")
        val database = node.node("database").string ?: error("mysql database is null")

        Database.connect("jdbc:mysql://$address/$database", "com.mysql.cj.jdbc.Driver", username, password)
    }
}