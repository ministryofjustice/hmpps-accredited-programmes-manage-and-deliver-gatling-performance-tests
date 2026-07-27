package uk.gov.justice.digital.hmpps.team.acp.jdbc

import io.gatling.javaapi.core.FeederBuilder
import io.gatling.javaapi.jdbc.JdbcDsl
import uk.gov.justice.digital.hmpps.config.DbConfig
import uk.gov.justice.digital.hmpps.team.acp.model.GroupAllocationSimulationSession

class GroupAllocationFeeder(
    private val dbConfig: DbConfig = DbConfig(),
) {
    fun getJdbcFeederForGroupAllocation(): FeederBuilder<Any> {
        val feederQuery = """
            SELECT DISTINCT ON (gwiv.referral_id)
                   pg.id         AS ${GroupAllocationSimulationSession.GROUP_ID.sessionKey},
                   gwiv.referral_id AS ${GroupAllocationSimulationSession.REFERRAL_ID.sessionKey}
            FROM group_waitlist_item_view gwiv
            JOIN programme_group pg
              ON pg.region_name = gwiv.region_name
             AND pg.deleted_at IS NULL
            WHERE gwiv.status = 'Awaiting allocation'
              AND gwiv.active_programme_group_id IS NULL
              AND gwiv.region_name = 'Greater Manchester'
            ORDER BY gwiv.referral_id, random()
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
