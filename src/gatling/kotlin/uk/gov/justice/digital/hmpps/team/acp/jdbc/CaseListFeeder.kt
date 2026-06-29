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
            Where rciv.id in ('0382b915-27fd-47e3-ad3e-66f3f33c1771', 'c3d7f959-5487-478e-a2c4-bae8bddba618')
            """

        return JdbcDsl.jdbcFeeder(
            "jdbc:postgresql://localhost:${dbConfig.dbPort}/${dbConfig.dbName}",
            dbConfig.dbUsername,
            dbConfig.dbPassword,
            feederQuery
        ).random()
    }
}