package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.jdbc.ScheduleOverviewFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.ScheduleOverviewPauseConfig
import java.time.Duration

class ScheduleOverviewScenarioService(
    private val scheduleOverviewFeeder: ScheduleOverviewFeeder = ScheduleOverviewFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val signInService: SignInService = SignInService(),
    private val pageOrchestrationService: ScheduleOverviewPageOrchestrationService = ScheduleOverviewPageOrchestrationService(),
) {
    fun buildScenario(
        scenarioName: String,
        pauses: ScheduleOverviewPauseConfig,
        journeyDuration: Duration,
    ): ScenarioBuilder {
        // Authenticate via HMPPS-Auth
        val authenticate =
            httpRequestHelper.acpAuthCookie
                ?.let { CoreDsl.exec(HttpDsl.addCookie(it)) }
                ?: signInService.signIn()

        val singleScheduleOverviewJourney =
            CoreDsl
                .feed(scheduleOverviewFeeder.getJdbcFeederForScheduleOverview())
                .pause(pauses.beforeStart.first, pauses.beforeStart.second)
                .exec(pageOrchestrationService.getScheduleOverviewPageAndDoChecks())
                .pause(pauses.onSchedulePage.first, pauses.onSchedulePage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .during(journeyDuration)
            .on(
                CoreDsl.exitBlockOnFail().on(singleScheduleOverviewJourney),
            )
    }
}
