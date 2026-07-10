package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupPauseConfig
import kotlin.Long

val NO_OF_CREATE_GROUP_USERS: Int = System.getProperty("create_group_concurrent_users")?.toIntOrNull() ?: 2
val CREATE_GROUP_TEST_DURATION_MINUTES: Long = System.getProperty("create_group_test_duration_minutes")?.toLongOrNull() ?: 5L

val createGroupPauseConfig = CreateGroupPauseConfig(
    beforeStart = 0L to 2L,
    onCreateGroupPage = 3L to 6L,
    afterCreateGroupPage = 4L to 12L,
    onCreateGroupCodePage = 5L to 16L,
    afterCreateGroupCodePage = 6L to 18L,
    onGroupStartDatePage = 7L to 20L,
    afterGroupStartDatePage = 8L to 22L,
    onGroupDaysAndTimesPage = 9L to 24L,
    afterGroupDaysAndTimesPage = 10L to 26L,
    onGroupCohortPage = 11L to 28L,
    afterGroupCohortPage = 12L to 30L,
    onGroupGenderPage = 13L to 32L,
    afterGroupGenderPage = 14L to 34L,
    onProbationDeliveryUnitPage = 15L to 36L,
    afterProbationDeliveryUnitPage = 16L to 38L,
    onDeliveryLocationPage = 17L to 40L,
    afterDeliveryLocationPage = 18L to 42L,
    onGroupFacilitatorsPage = 19L to 44L,
    afterGroupFacilitatorsPage = 20L to 46L,
    onGroupReviewDetailsPage = 22L to 48L
    )

const val CREATE_GROUP_CODE: String = "PERF TEST \${System.currentTimeMillis()}-\${session.userId()}"
const val CREATE_GROUP_DATE: String = "17/7/2027"
const val DAYS_OF_WEEK_MONDAY: String = "MONDAY"
const val MONDAY_HOUR_ONE: String = "1"
const val MONDAY_AMPM_PM: String = "PM"
const val GROUP_COHORT: String = "GENERAL"
const val GROUP_SEX: String = "MALE"
const val GROUP_PDU = """{"code":"N50ALL","name":"All Greater Manchester"}"""
const val GROUP_LOCATION = """{"code":"N501CCO", "name":"Oldham Interventions Office"}"""
const val GROUP_TREATMENT_MANAGER = """{"facilitator":"Unallocated Staff", "facilitatorCode":"N50HHIU", "teamName":"HMP Hindley", "teamCode":"N50HHI", "teamMemberType":"TREATMENT_MANAGER"}"""
const val GROUP_FACILITATOR = """{"facilitator":"Unallocated Staff", "facilitatorCode":"N50HHIU", "teamName":"HMP Hindley", "teamCode":"N50HHI", "teamMemberType":"REGULAR_FACILITATOR"}"""