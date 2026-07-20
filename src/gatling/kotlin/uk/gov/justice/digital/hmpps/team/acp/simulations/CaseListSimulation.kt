package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.CASE_LIST_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_CASE_LIST_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.caseListPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.CaseListScenarioService
import java.time.Duration

class CaseListSimulation(
    caseListScenarioService: CaseListScenarioService = CaseListScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(CASE_LIST_TEST_DURATION_MINUTES)
        val caseListScenario =
            caseListScenarioService.buildScenario(
                scenarioName = "Case List Journey",
                caseListPauseConfig,
                testDuration,
            )
        setUp(
            caseListScenario.injectClosed(
                constantConcurrentUsers(NO_OF_CASE_LIST_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
