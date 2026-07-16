package uk.gov.justice.digital.hmpps.team.acp.jdbc

import uk.gov.justice.digital.hmpps.config.DbConfig
import java.sql.DriverManager

class CreateGroupRepository(
    private val dbConfig: DbConfig = DbConfig()
) {

    fun findGroupIdByCode(groupCode: String): Long? {
        val sql = """
            select pg.id
            from programme_group pg
            where pg.code = ?
        """.trimIndent()

        DriverManager.getConnection(
            "jdbc:postgresql://localhost:${dbConfig.dbPort}/${dbConfig.dbName}",
            dbConfig.dbUsername,
            dbConfig.dbPassword
        ).use { connection ->

            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, groupCode)

                statement.executeQuery().use { result ->
                    return if (result.next()) {
                        result.getLong("id")
                    } else {
                        null
                    }
                }
            }
        }
    }
}