package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.helper.ScheduleOverviewSelectors
import uk.gov.justice.digital.hmpps.team.acp.model.ScheduleOverviewSimulationSession

class ScheduleOverviewPageOrchestrationService {
    fun getScheduleOverviewPageAndDoChecks() =
        HttpDsl
            .http("GET - Schedule overview Page")
            .get { session ->
                val groupId = session.getString(ScheduleOverviewSimulationSession.GROUP_ID.sessionKey)
                "/group/$groupId/schedule-overview"
            }.check(
                HttpDsl.status().`is` { 200 },
                ScheduleOverviewSelectors.headingCheck(),
            )
}
