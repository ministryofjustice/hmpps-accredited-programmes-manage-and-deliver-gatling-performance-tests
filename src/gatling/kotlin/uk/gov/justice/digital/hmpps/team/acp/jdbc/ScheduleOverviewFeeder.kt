package uk.gov.justice.digital.hmpps.team.acp.jdbc

import io.gatling.javaapi.core.FeederBuilder
import io.gatling.javaapi.jdbc.JdbcDsl
import uk.gov.justice.digital.hmpps.config.DbConfig
import uk.gov.justice.digital.hmpps.team.acp.model.ScheduleOverviewSimulationSession

class ScheduleOverviewFeeder(
    private val dbConfig: DbConfig = DbConfig(),
) {
    fun getJdbcFeederForScheduleOverview(): FeederBuilder<Any> {
        val feederQuery = """select distinct pg.id as ${ScheduleOverviewSimulationSession.GROUP_ID.sessionKey}
            from programme_group pg
            join programme_group_session_slot pgss on pgss.programme_group_id = pg.id
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
