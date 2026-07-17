package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.team.acp.jdbc.CaseListFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListPauseConfig

class CaseListScenarioService(
    private val caseListFeeder: CaseListFeeder = CaseListFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val pageOrchestrationService: CaseListPageOrchestrationService = CaseListPageOrchestrationService()
) {

    fun buildScenario(
        scenarioName: String,
        pauses: CaseListPauseConfig

    ): ScenarioBuilder {
        val cookie = checkNotNull(httpRequestHelper.acpAuthCookie) {
            "acpAuthCookie is null — set hmpps-accredited-programmes-manage-and-deliver-ui.session " +
                "in local.properties or pass it as -Dhmpps-accredited-programmes-manage-and-deliver-ui.session=<session-cookie>"
        }

        val caseListChainBuilder = CoreDsl.feed(caseListFeeder.getJdbcFeederForCaseList())
            .exec(HttpDsl.addCookie(cookie))
            .pause(pauses.beforeStart.first, pauses.beforeStart.second)
            .exec(
                pageOrchestrationService.getOpenReferralsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onCaseListPage.first, pauses.onCaseListPage.second)
            .exec(
                pageOrchestrationService.getReferralDetailsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onReferralDetailPage.first, pauses.onReferralDetailPage.second)
            .exec (
                pageOrchestrationService.getRisksAndNeedsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onRisksAndNeedsPage.first, pauses.onRisksAndNeedsPage.second)
            .exec (
                pageOrchestrationService.getProgrammeNeedsIdentifierPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onProgrammeNeedsIdentifierPage.first, pauses.onProgrammeNeedsIdentifierPage.second)
            .exec (
                pageOrchestrationService.getAvailabilityAndMotivationPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onAvailabilityAndMotivationPage.first, pauses.onAvailabilityAndMotivationPage.second)
            .exec (
                pageOrchestrationService.getAttendanceHistoryPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onAttendanceHistoryPage.first, pauses.onAttendanceHistoryPage.second)
            .exec (
                pageOrchestrationService.getStatusHistoryPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onStatusHistoryPage.first, pauses.onStatusHistoryPage.second)
            .exec (
                pageOrchestrationService.getUpdateLearningDisabilitiesAndChallengesPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onLearningDisabilitiesAndChallengesPage.first, pauses.onLearningDisabilitiesAndChallengesPage.second)
            .exec (
                pageOrchestrationService.postUpdateLearningDisabilitiesAndChallengesPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterLearningDisabilitiesAndChallengesPage.first, pauses.afterLearningDisabilitiesAndChallengesPage.second)
            .exec(
                pageOrchestrationService.getReferralDetailsWithLdcUpdatedPageAndDoChecks())
            .exitHereIfFailed()
            .pause(pauses.onReferralDetailsWithLdcUpdatedPage.first, pauses.onReferralDetailsWithLdcUpdatedPage.second)
            .exec (
                pageOrchestrationService.getChangeCohortPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onCohortUpdatePage.first, pauses.onCohortUpdatePage.second)
            .exec (
                pageOrchestrationService.postChangeCohortPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterCohortUpdatePage.first, pauses.afterCohortUpdatePage.second)
            .exec(
                pageOrchestrationService.getReferralDetailsWithCohortUpdatedPageAndDoChecks())
            .exitHereIfFailed()
            .pause(pauses.onReferralDetailsWithCohortUpdatedPage.first, pauses.onReferralDetailsWithCohortUpdatedPage.second)
            .exec (
                pageOrchestrationService.getUpdateReferralStatusPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onUpdateReferralStatusPage.first, pauses.onUpdateReferralStatusPage.second)

        return CoreDsl.scenario(scenarioName)
            .exec(caseListChainBuilder)
    }
}