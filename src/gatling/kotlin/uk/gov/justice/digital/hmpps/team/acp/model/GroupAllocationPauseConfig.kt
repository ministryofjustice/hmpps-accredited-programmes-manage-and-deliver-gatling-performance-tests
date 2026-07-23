package uk.gov.justice.digital.hmpps.team.acp.model

data class GroupAllocationPauseConfig(
    val beforeStart: Pair<Long, Long>,
    val onWaitlistPage: Pair<Long, Long>,
    val afterWaitlistPage: Pair<Long, Long>,
    val onAddToGroupConfirmPage: Pair<Long, Long>,
    val afterAddToGroupConfirmPage: Pair<Long, Long>,
    val onScheduledStatusDetailsPage: Pair<Long, Long>,
    val afterScheduledStatusDetailsPage: Pair<Long, Long>,
    val onAllocatedListPage: Pair<Long, Long>,
    val afterAllocatedListPage: Pair<Long, Long>,
)
