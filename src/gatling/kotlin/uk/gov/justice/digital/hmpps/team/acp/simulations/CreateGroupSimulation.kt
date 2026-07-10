package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.service.CreateGroupScenarioService
import uk.gov.justice.digital.hmpps.team.acp.constants.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class CreateGroupSimulation(createGroupScenarioService: CreateGroupScenarioService = CreateGroupScenarioService()): BaseSimulationFrontEndRoutes() {
    init {
        val createGroupScenario = createGroupScenarioService.buildScenario(
            scenarioName = "Create Group Journey",
            createGroupPauseConfig
        )
        setUp(
            createGroupScenario.injectClosed(
                CoreDsl.constantConcurrentUsers(NO_OF_CREATE_GROUP_USERS)
                    .during(CREATE_GROUP_TEST_DURATION_MINUTES.minutes.toJavaDuration())
            )
        ).protocols(httpProtocol)
            .maxDuration(CREATE_GROUP_TEST_DURATION_MINUTES.minutes.toJavaDuration())
    }
}