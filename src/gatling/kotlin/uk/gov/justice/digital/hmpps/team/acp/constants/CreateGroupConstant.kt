package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupPauseConfig
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val NO_OF_CREATE_GROUP_USERS: Int = ConfigResolver.get("create_group_concurrent_users")?.toIntOrNull() ?: 1
val CREATE_GROUP_TEST_DURATION_MINUTES: Long =
    ConfigResolver.get("create_group_test_duration_minutes")?.toLongOrNull() ?: 5L

val createGroupPauseConfig =
    CreateGroupPauseConfig(
        beforeStart = 0L to 2L,
        onCreateGroupPage = 2L to 5L,
        afterCreateGroupPage = 2L to 5L,
        onCreateGroupCodePage = 2L to 5L,
        afterCreateGroupCodePage = 2L to 5L,
        onGroupStartDatePage = 2L to 5L,
        afterGroupStartDatePage = 2L to 5L,
        onGroupDaysAndTimesPage = 3L to 6L,
        afterGroupDaysAndTimesPage = 3L to 6L,
        onGroupCohortPage = 3L to 6L,
        afterGroupCohortPage = 3L to 6L,
        onGroupGenderPage = 3L to 6L,
        afterGroupGenderPage = 3L to 6L,
        onProbationDeliveryUnitPage = 3L to 6L,
        afterProbationDeliveryUnitPage = 3L to 6L,
        onDeliveryLocationPage = 3L to 6L,
        afterDeliveryLocationPage = 3L to 6L,
        onGroupFacilitatorsPage = 3L to 6L,
        afterGroupFacilitatorsPage = 3L to 6L,
        onGroupReviewDetailsPage = 3L to 6L,
        onGroupCreatedPage = 3L to 6L,
    )

fun generateCreateGroupDate(): String = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("d/M/yyyy"))

const val DAYS_OF_WEEK_MONDAY: String = "MONDAY"
const val MONDAY_HOUR_ONE: String = "1"
const val MONDAY_AMPM_PM: String = "PM"
const val GROUP_COHORT: String = "GENERAL"
const val GROUP_SEX: String = "MALE"
const val GROUP_PDU = """{"code":"N50ALL","name":"All Greater Manchester"}"""
const val GROUP_LOCATION = """{"code":"N501CCO", "name":"Oldham Interventions Office"}"""
const val GROUP_TREATMENT_MANAGER =
    """{"facilitator":"Unallocated Staff", "facilitatorCode":"N50HHIU", "teamName":"HMP Hindley", "teamCode":"N50HHI", "teamMemberType":"TREATMENT_MANAGER"}"""
const val GROUP_FACILITATOR =
    """{"facilitator":"Unallocated Staff", "facilitatorCode":"N50HHIU", "teamName":"HMP Hindley", "teamCode":"N50HHI", "teamMemberType":"REGULAR_FACILITATOR"}"""
