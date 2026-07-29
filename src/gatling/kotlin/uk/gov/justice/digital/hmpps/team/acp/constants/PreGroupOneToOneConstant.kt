package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver
import uk.gov.justice.digital.hmpps.team.acp.model.PreGroupOneToOnePauseConfig

val NO_OF_PRE_GROUP_ONE_TO_ONE_USERS: Int = ConfigResolver.get("pre_group_one_to_one_concurrent_users")?.toIntOrNull() ?: 2
val PRE_GROUP_ONE_TO_ONE_TEST_DURATION_MINUTES: Long =
    ConfigResolver.get("pre_group_one_to_one_test_duration_minutes")?.toLongOrNull() ?: 3L

val preGroupOneToOnePauseConfig =
    PreGroupOneToOnePauseConfig(
        beforeStart = 0L to 2L,
        onSessionAndAttendancePage = 3L to 8L,
        onScheduleSessionTypePage = 4L to 9L,
        afterScheduleSessionTypePage = 5L to 10L,
        onScheduleSessionDetailsPage = 6L to 11L,
        afterScheduleSessionDetailsPage = 7L to 12L,
        onReviewYourSessionDetailsPage = 8L to 13L,
    )
const val PRE_GROUP_ONE_TO_ONE_SESSION_ID: String = "33a740fb-a7b0-42e3-ba6b-e2b3ec25c795"
const val PRE_GROUP_ONE_TO_ONE_SESSION: String = "1bcaf371-e624-4034-a13b-5ae2e9921bd4+SCHEDULED+Pre-group one-to-one+ONE_TO_ONE"
const val PRE_GROUP_ONE_TO_ONE_SESSION_DATE: String = "27/09/2026"
const val PRE_GROUP_ONE_TO_ONE_SESSION_START_TIME_HOUR: String = "1"
const val PRE_GROUP_ONE_TO_ONE_SESSION_START_TIME_PART_OF_DAY: String = "PM"
const val PRE_GROUP_ONE_TO_ONE_SESSION_END_TIME_HOUR: String = "3"
const val PRE_GROUP_ONE_TO_ONE_SESSION_END_TIME_PART_OF_DAY: String = "PM"
const val PRE_GROUP_ONE_TO_ONE_SESSION_DETAILS_WHO: String = "fc2e8364-3fd9-4686-98a7-efd1dbaf3b82 + Craig Kuhic"
const val PRE_GROUP_ONE_TO_ONE_SESSION_DETAILS_FACILITATOR: String = "{\"facilitator\":\"Unallocated Staff\", \"facilitatorCode\":\"N50HHIU\", \"teamName\":\"HMP Hindley\", \"teamCode\":\"N50HHI\"}"
