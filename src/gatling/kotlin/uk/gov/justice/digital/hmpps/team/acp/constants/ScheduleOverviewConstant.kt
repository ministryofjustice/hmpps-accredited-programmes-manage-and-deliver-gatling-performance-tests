package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver
import uk.gov.justice.digital.hmpps.team.acp.model.ScheduleOverviewPauseConfig

val NO_OF_SCHEDULE_OVERVIEW_USERS: Int = ConfigResolver.get("schedule_overview_concurrent_users")?.toIntOrNull() ?: 5
val SCHEDULE_OVERVIEW_TEST_DURATION_MINUTES: Long =
    ConfigResolver.get("schedule_overview_test_duration_minutes")?.toLongOrNull() ?: 5L

val scheduleOverviewPauseConfig =
    ScheduleOverviewPauseConfig(
        beforeStart = 0L to 2L,
        onSchedulePage = 3L to 8L,
    )
