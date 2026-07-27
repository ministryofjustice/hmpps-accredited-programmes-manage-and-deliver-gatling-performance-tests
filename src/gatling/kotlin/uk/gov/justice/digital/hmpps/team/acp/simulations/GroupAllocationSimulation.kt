package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.rampUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_ALLOCATION_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_GROUP_ALLOCATION_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.groupAllocationPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.GroupAllocationScenarioService
import java.time.Duration

class GroupAllocationSimulation(
    groupAllocationScenarioService: GroupAllocationScenarioService = GroupAllocationScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(GROUP_ALLOCATION_TEST_DURATION_MINUTES)
        val groupAllocationScenario =
            groupAllocationScenarioService.buildScenario(
                scenarioName = "Group Allocation Journey",
                groupAllocationPauseConfig,
                testDuration,
            )
        setUp(
            groupAllocationScenario.injectOpen(
                rampUsers(NO_OF_GROUP_ALLOCATION_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
