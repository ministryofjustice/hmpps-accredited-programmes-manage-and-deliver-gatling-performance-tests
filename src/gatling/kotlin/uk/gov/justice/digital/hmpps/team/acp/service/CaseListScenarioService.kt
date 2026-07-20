package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.jdbc.CaseListFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListPauseConfig
import java.time.Duration

class CaseListScenarioService(
    private val caseListFeeder: CaseListFeeder = CaseListFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val signInService: SignInService = SignInService(),
    private val pageOrchestrationService: CaseListPageOrchestrationService = CaseListPageOrchestrationService(),
) {
    fun buildScenario(
        scenarioName: String,
        pauses: CaseListPauseConfig,
        journeyDuration: Duration,
    ): ScenarioBuilder {
        // Authenticate via HMPPS-Auth
        val authenticate =
            httpRequestHelper.acpAuthCookie
                ?.let { CoreDsl.exec(HttpDsl.addCookie(it)) }
                ?: signInService.signIn()

        // Traverse case list and referral details pages
        val singleCaseListJourney =
            CoreDsl
                .feed(caseListFeeder.getJdbcFeederForCaseList())
                .pause(pauses.beforeStart.first, pauses.beforeStart.second)
                .exec(pageOrchestrationService.getOpenReferralsPageAndDoChecks())
                .pause(pauses.onCaseListPage.first, pauses.onCaseListPage.second)
                .exec(pageOrchestrationService.getReferralDetailsPageAndDoChecks())
                .pause(pauses.onReferralDetailPage.first, pauses.onReferralDetailPage.second)
                .exec(pageOrchestrationService.getRisksAndNeedsPageAndDoChecks())
                .pause(pauses.onRisksAndNeedsPage.first, pauses.onRisksAndNeedsPage.second)
                .exec(pageOrchestrationService.getProgrammeNeedsIdentifierPageAndDoChecks())
                .pause(pauses.onProgrammeNeedsIdentifierPage.first, pauses.onProgrammeNeedsIdentifierPage.second)
                .exec(pageOrchestrationService.getAvailabilityAndMotivationPageAndDoChecks())
                .pause(pauses.onAvailabilityAndMotivationPage.first, pauses.onAvailabilityAndMotivationPage.second)
                .exec(pageOrchestrationService.getAttendanceHistoryPageAndDoChecks())
                .pause(pauses.onAttendanceHistoryPage.first, pauses.onAttendanceHistoryPage.second)
                .exec(pageOrchestrationService.getStatusHistoryPageAndDoChecks())
                .pause(pauses.onStatusHistoryPage.first, pauses.onStatusHistoryPage.second)
                .exec(pageOrchestrationService.getUpdateLearningDisabilitiesAndChallengesPageAndDoChecks())
                .pause(pauses.onLearningDisabilitiesAndChallengesPage.first, pauses.onLearningDisabilitiesAndChallengesPage.second)
                .exec(pageOrchestrationService.postUpdateLearningDisabilitiesAndChallengesPageAndDoChecks())
                .pause(pauses.afterLearningDisabilitiesAndChallengesPage.first, pauses.afterLearningDisabilitiesAndChallengesPage.second)
                .exec(pageOrchestrationService.getReferralDetailsWithLdcUpdatedPageAndDoChecks())
                .pause(pauses.onReferralDetailsWithLdcUpdatedPage.first, pauses.onReferralDetailsWithLdcUpdatedPage.second)
                .exec(pageOrchestrationService.getChangeCohortPageAndDoChecks())
                .pause(pauses.onCohortUpdatePage.first, pauses.onCohortUpdatePage.second)
                .exec(pageOrchestrationService.postChangeCohortPageAndDoChecks())
                .pause(pauses.afterCohortUpdatePage.first, pauses.afterCohortUpdatePage.second)
                .exec(pageOrchestrationService.getReferralDetailsWithCohortUpdatedPageAndDoChecks())
                .pause(pauses.onReferralDetailsWithCohortUpdatedPage.first, pauses.onReferralDetailsWithCohortUpdatedPage.second)
                .exec(pageOrchestrationService.getUpdateReferralStatusPageAndDoChecks())
                .pause(pauses.onUpdateReferralStatusPage.first, pauses.onUpdateReferralStatusPage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .during(journeyDuration)
            .on(
                CoreDsl.exitBlockOnFail().on(singleCaseListJourney),
            )
    }
}
