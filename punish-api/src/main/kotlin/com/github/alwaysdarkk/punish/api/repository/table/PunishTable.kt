package com.github.alwaysdarkk.punish.api.repository.table

import org.jetbrains.exposed.v1.core.Table

object PunishTable : Table() {
    val id = varchar("id", 5)
    val playerName = varchar("playerName", 16)
    val author = varchar("author", 16)
    val reasonId = text("reasonId")
    val evidence = text("evidence").nullable()
    val expireDate = long("expireDate").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "punishes")
}