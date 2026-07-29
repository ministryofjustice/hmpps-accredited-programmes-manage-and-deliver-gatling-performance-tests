package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.rampUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.FULL_SIMULATION_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_CASE_LIST_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_CREATE_GROUP_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_GROUP_ALLOCATION_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_GROUP_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_SCHEDULE_OVERVIEW_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.caseListPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.constants.createGroupPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.constants.groupAllocationPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.constants.groupDetailsPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.constants.scheduleOverviewPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.CaseListScenarioService
import uk.gov.justice.digital.hmpps.team.acp.service.CreateGroupScenarioService
import uk.gov.justice.digital.hmpps.team.acp.service.GroupAllocationScenarioService
import uk.gov.justice.digital.hmpps.team.acp.service.GroupDetailsScenarioService
import uk.gov.justice.digital.hmpps.team.acp.service.ScheduleOverviewScenarioService
import java.time.Duration

class FullSimulation(
    caseListScenarioService: CaseListScenarioService = CaseListScenarioService(),
    createGroupScenarioService: CreateGroupScenarioService = CreateGroupScenarioService(),
    groupAllocationScenarioService: GroupAllocationScenarioService = GroupAllocationScenarioService(),
    groupDetailsScenarioService: GroupDetailsScenarioService = GroupDetailsScenarioService(),
    scheduleOverviewScenarioService: ScheduleOverviewScenarioService = ScheduleOverviewScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(FULL_SIMULATION_TEST_DURATION_MINUTES)
        val caseListScenario =
            caseListScenarioService.buildScenario(
                scenarioName = "Case List Journey",
                caseListPauseConfig,
                testDuration,
            )
        val createGroupScenario =
            createGroupScenarioService.buildScenario(
                scenarioName = "Create Group Journey",
                createGroupPauseConfig,
                testDuration,
            )
        val groupAllocationScenario =
            groupAllocationScenarioService.buildScenario(
                scenarioName = "Group Allocation Journey",
                groupAllocationPauseConfig,
                testDuration,
            )
        val getGroupDetailsScenario =
            groupDetailsScenarioService.buildScenario(
                scenarioName = "Group Details Journey",
                groupDetailsPauseConfig,
                testDuration,
            )
        val scheduleOverviewScenario =
            scheduleOverviewScenarioService.buildScenario(
                scenarioName = "Schedule Overview Journey",
                scheduleOverviewPauseConfig,
                testDuration,
            )

        setUp(
            caseListScenario.injectClosed(
                constantConcurrentUsers(NO_OF_CASE_LIST_USERS).during(testDuration),
            ),
            createGroupScenario.injectClosed(
                constantConcurrentUsers(NO_OF_CREATE_GROUP_USERS).during(testDuration),
            ),
            groupAllocationScenario.injectOpen(
                rampUsers(NO_OF_GROUP_ALLOCATION_USERS).during(testDuration),
            ),
            getGroupDetailsScenario.injectClosed(
                constantConcurrentUsers(NO_OF_GROUP_USERS).during(testDuration),
            ),
            scheduleOverviewScenario.injectClosed(
                constantConcurrentUsers(NO_OF_SCHEDULE_OVERVIEW_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
