package com.github.alwaysdarkk.punish.api.repository

import com.github.alwaysdarkk.punish.api.data.punish.Punish
import com.github.alwaysdarkk.punish.api.repository.provider.RepositoryProvider.coroutineScope
import com.github.alwaysdarkk.punish.api.repository.table.PunishTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object PunishRepository {

    fun insert(punish: Punish) {
        coroutineScope.launch {
            transaction {
                PunishTable.insert {
                    it[id] = punish.id
                    it[playerName] = punish.playerName
                    it[author] = punish.author
                    it[reasonId] = punish.reasonId
                    it[evidence] = punish.evidence
                    it[expireDate] = punish.expireDate
                }
            }
        }
    }

    fun delete(punish: Punish) {
        coroutineScope.launch {
            transaction { PunishTable.deleteWhere { id eq punish.id } }
        }
    }

    suspend fun findAll(): List<Punish> = withContext(Dispatchers.IO) {
        transaction { PunishTable.selectAll().map { it.toPunish() } }
    }

    private fun ResultRow.toPunish(): Punish = Punish(
        this[PunishTable.id],
        this[PunishTable.playerName],
        this[PunishTable.author],
        this[PunishTable.reasonId],
        this[PunishTable.evidence],
        this[PunishTable.expireDate]
    )
}