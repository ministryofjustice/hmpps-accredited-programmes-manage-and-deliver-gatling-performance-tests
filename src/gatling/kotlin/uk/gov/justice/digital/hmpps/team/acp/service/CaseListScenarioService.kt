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
        pauseOnReferralDetailPage: Pair<Long, Long>,
        pauseOnRisksAndNeedsPage: Pair<Long, Long>,
        pauseOnProgrammeNeedsIdentifierPage: Pair<Long, Long>,
        pauseOnAvailabilityAndMotivationPage: Pair<Long, Long>,
        pauseOnAttendanceHistoryPage: Pair<Long, Long>,
        pauseOnStatusHistoryPage: Pair<Long, Long>



        ): ScenarioBuilder {
        val caseListChainBuilder = CoreDsl.feed(caseListFeeder.getJdbcFeederForCaseList())
            .exec(HttpDsl.addCookie(httpRequestHelper.acpAuthCookie!!))
            .pause(pauseBeforeStart.first, pauseBeforeStart.second)
            .exec(
                pageOrchestrationService.hitOpenReferralsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnCaseListPage.first, pauseOnCaseListPage.second)
            .exec(
                pageOrchestrationService.hitReferralDetailsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnReferralDetailPage.first, pauseOnReferralDetailPage.second)
            .exec (
                pageOrchestrationService.hitRisksAndNeedsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnRisksAndNeedsPage.first, pauseOnRisksAndNeedsPage.second)
            .exec (
                pageOrchestrationService.hitProgrammeNeedsIdentifierPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnProgrammeNeedsIdentifierPage.first, pauseOnProgrammeNeedsIdentifierPage.second)
            .exec (
                pageOrchestrationService.hitAvailabilityAndMotivationPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnAvailabilityAndMotivationPage.first, pauseOnAvailabilityAndMotivationPage.second)
            .exec (
                pageOrchestrationService.hitAttendanceHistoryPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnAttendanceHistoryPage.first, pauseOnAttendanceHistoryPage.second)
            .exec (
                pageOrchestrationService.hitStatusHistoryPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauseOnStatusHistoryPage.first, pauseOnStatusHistoryPage.second)


        return CoreDsl.scenario(scenarioName)
            .exec(caseListChainBuilder)
    }
}