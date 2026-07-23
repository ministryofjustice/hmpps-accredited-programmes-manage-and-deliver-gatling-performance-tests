package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_SCHEDULE_OVERVIEW_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.SCHEDULE_OVERVIEW_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.scheduleOverviewPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.ScheduleOverviewScenarioService
import java.time.Duration

class ScheduleOverviewSimulation(
    scheduleOverviewScenarioService: ScheduleOverviewScenarioService = ScheduleOverviewScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(SCHEDULE_OVERVIEW_TEST_DURATION_MINUTES)
        val scheduleOverviewScenario =
            scheduleOverviewScenarioService.buildScenario(
                scenarioName = "Schedule Overview Journey",
                scheduleOverviewPauseConfig,
                testDuration,
            )
        setUp(
            scheduleOverviewScenario.injectClosed(
                constantConcurrentUsers(NO_OF_SCHEDULE_OVERVIEW_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
