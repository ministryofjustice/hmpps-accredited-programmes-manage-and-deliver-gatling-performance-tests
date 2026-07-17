package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListPauseConfig

val NO_OF_CASE_LIST_USERS: Int = ConfigResolver.get("concurrent_users")?.toIntOrNull() ?: 2
val TEST_DURATION_MINUTES: Long = ConfigResolver.get("test_duration_minutes")?.toLongOrNull() ?: 5L

val caseListPauseConfig =
    CaseListPauseConfig(
        beforeStart = 0L to 2L,
        onCaseListPage = 3L to 6L,
        onReferralDetailPage = 4L to 12L,
        onRisksAndNeedsPage = 5L to 16L,
        onProgrammeNeedsIdentifierPage = 6L to 18L,
        onAvailabilityAndMotivationPage = 7L to 20L,
        onAttendanceHistoryPage = 8L to 22L,
        onStatusHistoryPage = 9L to 24L,
        onLearningDisabilitiesAndChallengesPage = 10L to 26L,
        afterLearningDisabilitiesAndChallengesPage = 11L to 28L,
        onReferralDetailsWithLdcUpdatedPage = 12L to 30L,
        onCohortUpdatePage = 13L to 32L,
        afterCohortUpdatePage = 14L to 34L,
        onReferralDetailsWithCohortUpdatedPage = 15L to 36L,
        onUpdateReferralStatusPage = 16L to 38L,
    )

const val LDC_FLAG_MAY_NEED_AN_LDC_ADAPTED_PROGRAMME: String = "true"
const val CHANGE_COHORT_GENERAL_OFFENCE: String = "GENERAL_OFFENCE"
