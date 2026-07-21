package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.CREATE_GROUP_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_CREATE_GROUP_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.createGroupPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.CreateGroupScenarioService
import java.time.Duration

class CreateGroupSimulation(
    createGroupScenarioService: CreateGroupScenarioService = CreateGroupScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(CREATE_GROUP_TEST_DURATION_MINUTES)
        val createGroupScenario =
            createGroupScenarioService.buildScenario(
                scenarioName = "Create Group Journey",
                createGroupPauseConfig,
                testDuration,
            )
        setUp(
            createGroupScenario.injectClosed(
                constantConcurrentUsers(NO_OF_CREATE_GROUP_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
