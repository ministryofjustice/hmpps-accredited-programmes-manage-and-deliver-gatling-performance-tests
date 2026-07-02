package uk.gov.justice.digital.hmpps.team.acp.constants

import kotlin.to

const val noOfCaseListUsers = 2

val caseListPauseBeforeStart = 0L to 2L
val caseListPauseOnCaseListPage = 3L to 6L
val caseListPauseOnReferralDetailPage = 4L to 12L
val caseListPauseOnRisksAndNeedsPage = 5L to 16L
val caseListPauseOnProgrammeNeedsIdentifierPage = 6L to 18L
val caseListPauseOnAvailabilityAndMotivationPage = 7L to 20L
val caseListPauseOnAttendanceHistoryPage = 8L to 22L
val caseListPauseOnStatusHistoryPage = 9L to 24L
val caseListPauseOnLearningDisabilitiesAndChallengesPage = 10L to 26L
val caseListPauseAfterLearningDisabilitiesAndChallengesPage = 11L to 28L
val caseListPauseOnReferralDetailsWithLdcUpdatedPage = 12L to 30L
val caseListPauseOnCohortUpdatePage = 13L to 32L
val caseListPauseAfterCohortUpdatePage = 14L to 34L
val caseListPauseOnReferralDetailsWithCohortUpdatedPage = 15L to 36L
val caseListPauseOnUpdateReferralStatusPage = 16L to 38L

const val ldcFlag_MayNeedAnLDCAdaptedProgramme: String = "true"
const val changeCohort_GeneralOffence: String = "GENERAL_OFFENCE"

