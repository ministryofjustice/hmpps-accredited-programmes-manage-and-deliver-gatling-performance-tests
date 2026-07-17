package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_CASE_LIST_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.caseListPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.CaseListScenarioService
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class CaseListSimulation(
    caseListScenarioService: CaseListScenarioService = CaseListScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val caseListScenario =
            caseListScenarioService.buildScenario(
                scenarioName = "Case List Journey",
                caseListPauseConfig,
            )
        setUp(
            caseListScenario.injectClosed(
                constantConcurrentUsers(NO_OF_CASE_LIST_USERS).during(TEST_DURATION_MINUTES.minutes.toJavaDuration()),
            ),
        ).protocols(httpProtocol)
            .maxDuration(TEST_DURATION_MINUTES.minutes.toJavaDuration())
    }
}
