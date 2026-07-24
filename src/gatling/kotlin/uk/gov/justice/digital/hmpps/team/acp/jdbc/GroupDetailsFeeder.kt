package uk.gov.justice.digital.hmpps.team.acp.jdbc

import io.gatling.javaapi.core.FeederBuilder
import io.gatling.javaapi.jdbc.JdbcDsl
import uk.gov.justice.digital.hmpps.config.DbConfig
import uk.gov.justice.digital.hmpps.team.acp.model.GroupDetailsSimulationSession

class GroupDetailsFeeder(
    private val dbConfig: DbConfig = DbConfig(),
) {
    fun getJdbcFeederForGroup(): FeederBuilder<Any> {
        val feederQuery = """select pm.id as ${GroupDetailsSimulationSession.GROUP_ID.sessionKey}
            from programme_group pm
            where pm.region_name = 'Greater Manchester'
            """

        return JdbcDsl
            .jdbcFeeder(
                "jdbc:postgresql://localhost:${dbConfig.dbPort}/${dbConfig.dbName}",
                dbConfig.dbUsername,
                dbConfig.dbPassword,
                feederQuery,
            ).random()
    }
}
