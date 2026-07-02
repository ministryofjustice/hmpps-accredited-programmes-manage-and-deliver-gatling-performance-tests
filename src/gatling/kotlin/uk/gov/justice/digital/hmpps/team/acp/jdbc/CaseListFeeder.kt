package uk.gov.justice.digital.hmpps.team.acp.jdbc

import io.gatling.javaapi.core.FeederBuilder
import io.gatling.javaapi.jdbc.JdbcDsl
import uk.gov.justice.digital.hmpps.config.DbConfig
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListSimulationSession

//Need to update query

class CaseListFeeder (
    private val dbConfig: DbConfig = DbConfig()
) {
    fun getJdbcFeederForCaseList(): FeederBuilder<Any> {
        val feederQuery = """select rciv.id as ${CaseListSimulationSession.REFERRAL_ID.sessionKey}, 
            rciv.person_name as ${CaseListSimulationSession.REFERRAL_NAME.sessionKey} 
            from referral_caselist_item_view rciv
            where rciv.region_name = 'NPS North East'
            """

        return JdbcDsl.jdbcFeeder(
            "jdbc:postgresql://localhost:${dbConfig.dbPort}/${dbConfig.dbName}",
            dbConfig.dbUsername,
            dbConfig.dbPassword,
            feederQuery
        ).random()
    }
}