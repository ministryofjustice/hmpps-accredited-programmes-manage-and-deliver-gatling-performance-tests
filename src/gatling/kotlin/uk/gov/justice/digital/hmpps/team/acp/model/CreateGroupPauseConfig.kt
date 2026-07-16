package uk.gov.justice.digital.hmpps.team.acp.model

data class CreateGroupPauseConfig(
    val beforeStart: Pair<Long, Long>,
    val onCreateGroupPage: Pair<Long, Long>,
    val afterCreateGroupPage: Pair<Long, Long>,
    val onCreateGroupCodePage: Pair<Long, Long>,
    val afterCreateGroupCodePage: Pair<Long, Long>,
    val onGroupStartDatePage: Pair<Long, Long>,
    val afterGroupStartDatePage: Pair<Long, Long>,
    val onGroupDaysAndTimesPage: Pair<Long, Long>,
    val afterGroupDaysAndTimesPage: Pair<Long, Long>,
    val onGroupCohortPage: Pair<Long, Long>,
    val afterGroupCohortPage: Pair<Long, Long>,
    val onGroupGenderPage: Pair<Long, Long>,
    val afterGroupGenderPage: Pair<Long, Long>,
    val onProbationDeliveryUnitPage: Pair<Long, Long>,
    val afterProbationDeliveryUnitPage: Pair<Long, Long>,
    val onDeliveryLocationPage: Pair<Long, Long>,
    val afterDeliveryLocationPage: Pair<Long, Long>,
    val onGroupFacilitatorsPage: Pair<Long, Long>,
    val afterGroupFacilitatorsPage: Pair<Long, Long>,
    val onGroupReviewDetailsPage: Pair<Long, Long>,
    val afterGroupReviewDetailsPage: Pair<Long, Long>,
    val onGroupCreatedPage: Pair<Long, Long>
    )