package uk.gov.justice.digital.hmpps.team.acp.simulations

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import uk.gov.justice.digital.hmpps.BaseSimulationFrontEndRoutes
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_SESSIONS_AND_ATTENDANCE_TEST_DURATION_MINUTES
import uk.gov.justice.digital.hmpps.team.acp.constants.NO_OF_GROUP_SESSIONS_AND_ATTENDANCE_USERS
import uk.gov.justice.digital.hmpps.team.acp.constants.groupSessionsAndAttendancePauseConfig
import uk.gov.justice.digital.hmpps.team.acp.service.GroupSessionsAndAttendanceScenarioService
import java.time.Duration

class GroupSessionsAndAttendanceSimulation(
    getGroupSessionsAndAttendanceScenarioService: GroupSessionsAndAttendanceScenarioService = GroupSessionsAndAttendanceScenarioService(),
) : BaseSimulationFrontEndRoutes() {
    init {
        val testDuration = Duration.ofMinutes(GROUP_SESSIONS_AND_ATTENDANCE_TEST_DURATION_MINUTES)
        val getGroupSessionsAndAttendanceScenario =
            getGroupSessionsAndAttendanceScenarioService.buildScenario(
                scenarioName = "Group Sessions And Attendance Journey",
                groupSessionsAndAttendancePauseConfig,
                testDuration,
            )
        setUp(
            getGroupSessionsAndAttendanceScenario.injectClosed(
                constantConcurrentUsers(NO_OF_GROUP_SESSIONS_AND_ATTENDANCE_USERS).during(testDuration),
            ),
        ).protocols(httpProtocol)
            .maxDuration(testDuration)
    }
}
