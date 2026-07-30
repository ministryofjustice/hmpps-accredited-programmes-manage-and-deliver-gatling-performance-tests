package uk.gov.justice.digital.hmpps.team.acp.model

data class GroupSessionsAndAttendancePauseConfig(
    val beforeStart: Pair<Long, Long>,
    val onGroupSessionsAndAttendancePage: Pair<Long, Long>,
    val onSchedulePreGroupOneToOneSessionTypePage: Pair<Long, Long>,

    val onEditGroupCodePage: Pair<Long, Long>,
    val afterEditGroupCodePage: Pair<Long, Long>,
    val onEditGroupStartDatePage: Pair<Long, Long>,
    val afterEditGroupStartDatePage: Pair<Long, Long>,
    val onEditGroupDaysAndTimesPage: Pair<Long, Long>,
    val afterEditGroupDaysAndTimesPage: Pair<Long, Long>,
    val onEditGroupCohortPage: Pair<Long, Long>,
    val onEditGroupGenderPage: Pair<Long, Long>,
    val onEditGroupProbationDeliveryUnitPage: Pair<Long, Long>,
    val afterEditGroupProbationDeliveryUnitPage: Pair<Long, Long>,
    val onEditGroupDeliveryLocationPage: Pair<Long, Long>,
    val onEditGroupFacilitatorPage: Pair<Long, Long>,
)
