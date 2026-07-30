package uk.gov.justice.digital.hmpps.team.acp.jdbc

import io.gatling.javaapi.core.FeederBuilder
import io.gatling.javaapi.jdbc.JdbcDsl
import uk.gov.justice.digital.hmpps.config.DbConfig
import uk.gov.justice.digital.hmpps.team.acp.model.PreGroupOneToOneSimulationSession

class PreGroupOneToOneFeeder(
    private val dbConfig: DbConfig = DbConfig(),
) {
    fun getJdbcFeederForPreGroupOneToOne(): FeederBuilder<Any> {
        val feederQuery = """
            select pm.id as ${PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey}
            from programme_group pm
            where pm.region_name = 'Greater Manchester'
            """

        return JdbcDsl
            .jdbcFeeder(
                "jdbc:postgresql://localhost:${dbConfig.dbPort}/${dbConfig.dbName}",
                dbConfig.dbUsername,
                dbConfig.dbPassword,
                feederQuery,
            ).queue() // one row per user
    }
}
