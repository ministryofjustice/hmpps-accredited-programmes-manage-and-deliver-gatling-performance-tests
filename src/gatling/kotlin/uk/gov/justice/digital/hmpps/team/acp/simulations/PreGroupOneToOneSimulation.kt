package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_PRE_GROUP_ONE_TO_ONE_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.preGroupOneToOnePauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.PreGroupOneToOneScenarioService
import java.time.Duration

class PreGroupOneToOneSimulation(
    preGroupOneToOneScenarioService: PreGroupOneToOneScenarioService = PreGroupOneToOneScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(PRE_GROUP_ONE_TO_ONE_TEST_DURATION_MINUTES)
        val preGroupOneToOneScenario =
            preGroupOneToOneScenarioService.buildScenario(
                scenarioName = "Pre group one to one Journey",
                preGroupOneToOnePauseConfig,
                testDuration,
            )
        setUp(
            preGroupOneToOneScenario.injectClosed(
                constantConcurrentUsers(NO_OF_PRE_GROUP_ONE_TO_ONE_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
