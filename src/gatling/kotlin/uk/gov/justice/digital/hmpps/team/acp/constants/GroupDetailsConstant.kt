package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver
import uk.gov.justice.digital.hmpps.team.acp.model.GroupDetailsPauseConfig
import kotlin.Long

val NO_OF_GROUP_USERS: Int = ConfigResolver.get("group_details_concurrent_users")?.toIntOrNull() ?: 1
val GROUP_DETAILS_TEST_DURATION_MINUTES: Long =
    ConfigResolver.get("group_details_test_duration_minutes")?.toLongOrNull() ?: 5L

val groupDetailsPauseConfig =
    GroupDetailsPauseConfig(
        beforeStart = 0L to 2L,
        onGroupDetailsPage = 2L to 5L,
        onEditGroupCodePage = 2L to 5L,
        afterEditGroupCodePage = 2L to 5L,
        onEditGroupStartDatePage = 2L to 5L,
        afterEditGroupStartDatePage = 2L to 5L,
        onEditGroupDaysAndTimesPage = 2L to 5L,
        afterEditGroupDaysAndTimesPage = 2L to 5L,
        onEditGroupCohortPage = 2L to 5L,
        onEditGroupGenderPage = 2L to 5L,
        onEditGroupProbationDeliveryUnitPage = 2L to 5L,
        afterEditGroupProbationDeliveryUnitPage = 2L to 5L,
        onEditGroupDeliveryLocationPage = 2L to 5L,
        onEditGroupFacilitatorPage = 2L to 5L,
    )

const val RESCHEDULE_OTHER_SESSIONS: String = "true"
const val DAYS_OF_WEEK_TUESDAY: String = "TUESDAY"
const val TUESDAY_HOUR_ONE: String = "2"
const val TUESDAY_AMPM_PM: String = "PM"
const val EDIT_GROUP_COHORT: String = "GENERAL_LDC"
const val EDIT_GROUP_SEX: String = "MIXED"
const val EDIT_GROUP_PDU = """{"code":"N50ALL","name":"All Greater Manchester"}"""
const val EDIT_GROUP_LOCATION = """{"code":"N501CCO", "name":"Oldham Interventions Office"}"""
const val EDIT_GROUP_TREATMENT_MANAGER =
    """{"facilitator":"Unallocated Staff", "facilitatorCode":"N50HHIU", "teamName":"HMP Hindley", "teamCode":"N50HHI", "teamMemberType":"TREATMENT_MANAGER"}"""
const val EDIT_GROUP_FACILITATOR =
    """{"facilitator":"Unallocated Staff", "facilitatorCode":"N50HHIU", "teamName":"HMP Hindley", "teamCode":"N50HHI", "teamMemberType":"REGULAR_FACILITATOR"}"""
