package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.jdbc.PreGroupOneToOneFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.PreGroupOneToOnePauseConfig
import java.time.Duration

class PreGroupOneToOneScenarioService(

    private val preGroupOneToOneFeeder: PreGroupOneToOneFeeder = PreGroupOneToOneFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val signInService: SignInService = SignInService(),
    private val pageOrchestrationService: PreGroupOneToOnePageOrchestrationService = PreGroupOneToOnePageOrchestrationService(),
) {
    fun buildScenario(
        scenarioName: String,
        pauses: PreGroupOneToOnePauseConfig,
        journeyDuration: Duration,
    ): ScenarioBuilder {
        // Authenticate via HMPPS-Auth
        val authenticate =
            httpRequestHelper.acpAuthCookie
                ?.let { CoreDsl.exec(HttpDsl.addCookie(it)) }
                ?: signInService.signIn()

        val preGroupOneToOneJourney =
            CoreDsl
                .feed(preGroupOneToOneFeeder.getJdbcFeederForPreGroupOneToOne())
                .pause(pauses.beforeStart.first, pauses.beforeStart.second)
                .exec(pageOrchestrationService.getSessionAndAttendancePageAndDoChecks())
                .pause(pauses.onSessionAndAttendancePage.first, pauses.onSessionAndAttendancePage.second)
                .exec(pageOrchestrationService.getScheduleSessionTypePageAndDoChecks())
                .pause(pauses.onScheduleSessionTypePage.first, pauses.onScheduleSessionTypePage.second)
                .exec(pageOrchestrationService.postScheduleSessionTypePageAndDoChecks())
                .pause(pauses.afterScheduleSessionTypePage.first, pauses.afterScheduleSessionTypePage.second)
                .exec(pageOrchestrationService.getScheduleSessionDetailsPageAndDoChecks())
                .pause(pauses.onScheduleSessionDetailsPage.first, pauses.onScheduleSessionDetailsPage.second)
                .exec(pageOrchestrationService.postScheduleSessionDetailsPageAndDoChecks())
                .pause(pauses.afterScheduleSessionDetailsPage.first, pauses.afterScheduleSessionDetailsPage.second)
                .exec(pageOrchestrationService.getReviewYourSessionDetailsPageAndDoChecks())
                .pause(pauses.onReviewYourSessionDetailsPage.first, pauses.onReviewYourSessionDetailsPage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .during(journeyDuration)
            .on(
                CoreDsl.exitBlockOnFail().on(preGroupOneToOneJourney),
            )
    }
}
