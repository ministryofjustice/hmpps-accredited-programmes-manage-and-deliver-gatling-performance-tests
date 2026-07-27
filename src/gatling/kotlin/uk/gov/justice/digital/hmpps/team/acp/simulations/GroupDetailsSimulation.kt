package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_DETAILS_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_GROUP_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.groupDetailsPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.GroupDetailsScenarioService
import java.time.Duration

class GroupDetailsSimulation(
    getGroupDetailsScenarioService: GroupDetailsScenarioService = GroupDetailsScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(GROUP_DETAILS_TEST_DURATION_MINUTES)
        val getGroupDetailsScenario =
            getGroupDetailsScenarioService.buildScenario(
                scenarioName = "Group Details Journey",
                groupDetailsPauseConfig,
                testDuration,
            )
        setUp(
            getGroupDetailsScenario.injectClosed(
                constantConcurrentUsers(NO_OF_GROUP_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
