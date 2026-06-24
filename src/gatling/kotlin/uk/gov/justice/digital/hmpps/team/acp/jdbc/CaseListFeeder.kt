package uk.gov.justice.digital.hmpps.team.acp.jdbc

import io.gatling.javaapi.core.FeederBuilder
import io.gatling.javaapi.jdbc.JdbcDsl
import uk.gov.justice.digital.hmpps.config.DbConfig
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListSimulationSession

//Need to update query

class CaseListFeeder (
    private val dbConfig: DbConfig = DbConfig()
) {
    fun getJdbcFeederForEastMidland(): FeederBuilder<Any> {
        val feederQuery = """
            SELECT p.id as ${CaseListSimulationSession.PREMISE_ID.sessionKey}, 
            p.name as ${CaseListSimulationSession.PREMISE_NAME.sessionKey}
            FROM premises p
            LEFT JOIN approved_premises ap ON p.id = ap.premises_id
            LEFT JOIN rooms r ON r.premises_id=p.id
            LEFT JOIN beds b ON b.room_id=r.id
            LEFT JOIN probation_regions pr ON p.probation_region_id = pr.id
            LEFT JOIN ap_areas a ON pr.ap_area_id = a.id
            WHERE ap.supports_space_bookings = true
            AND (b.end_date IS NULL OR b.end_date > CURRENT_DATE)
            GROUP BY p.id, p.name
        """
        return JdbcDsl.jdbcFeeder(
            "jdbc:postgresql://localhost:${dbConfig.dbPort}/${dbConfig.dbName}",
            dbConfig.dbUsername,
            dbConfig.dbPassword,
            feederQuery
        ).random()
    }
}