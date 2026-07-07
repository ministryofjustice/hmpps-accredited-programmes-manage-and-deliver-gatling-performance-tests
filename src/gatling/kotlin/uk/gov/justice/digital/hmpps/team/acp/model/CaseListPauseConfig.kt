package uk.gov.justice.digital.hmpps.team.acp.model

data class CaseListPauseConfig(
    val beforeStart: Pair<Long, Long>,
    val onCaseListPage: Pair<Long, Long>,
    val onReferralDetailPage: Pair<Long, Long>,
    val onRisksAndNeedsPage: Pair<Long, Long>,
    val onProgrammeNeedsIdentifierPage: Pair<Long, Long>,
    val onAvailabilityAndMotivationPage: Pair<Long, Long>,
    val onAttendanceHistoryPage: Pair<Long, Long>,
    val onStatusHistoryPage: Pair<Long, Long>,
    val onLearningDisabilitiesAndChallengesPage: Pair<Long, Long>,
    val afterLearningDisabilitiesAndChallengesPage: Pair<Long, Long>,
    val onReferralDetailsWithLdcUpdatedPage: Pair<Long, Long>,
    val onCohortUpdatePage: Pair<Long, Long>,
    val afterCohortUpdatePage: Pair<Long, Long>,
    val onReferralDetailsWithCohortUpdatedPage: Pair<Long, Long>,
    val onUpdateReferralStatusPage: Pair<Long, Long>,
    )