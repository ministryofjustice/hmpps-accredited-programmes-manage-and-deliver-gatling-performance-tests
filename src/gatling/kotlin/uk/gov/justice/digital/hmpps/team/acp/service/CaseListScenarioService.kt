package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.team.acp.jdbc.CaseListFeeder

class CaseListScenarioService(
    private val caseListFeeder: CaseListFeeder = CaseListFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val pageOrchestrationService: CaseListPageOrchestrationService = CaseListPageOrchestrationService()
) {

    fun buildScenario(
        scenarioName: String,
        pauseBeforeStart: Pair<Long, Long>,
        pauseOnCaseListPage: Pair<Long, Long>,
    ): ScenarioBuilder {
        val caseListChainBuilder = CoreDsl.feed(caseListFeeder.getJdbcFeederForEastMidland())
            .exec(HttpDsl.addCookie(httpRequestHelper.acpAuthCookie!!))
            .pause(pauseBeforeStart.first, pauseBeforeStart.second)
            .exec(
                pageOrchestrationService.hitOpenReferralsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnCaseListPage.first, pauseOnCaseListPage.second)

        return CoreDsl.scenario(scenarioName)
            .exec(caseListChainBuilder)
    }
}