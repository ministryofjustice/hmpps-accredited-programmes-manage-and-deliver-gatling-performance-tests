package uk.gov.justice.digital.hmpps.team.acp.model

data class PreGroupOneToOnePauseConfig(
    val beforeStart: Pair<Long, Long>,
    val onSessionAndAttendancePage: Pair<Long, Long>,
    val onScheduleSessionTypePage: Pair<Long, Long>,
    val afterScheduleSessionTypePage: Pair<Long, Long>,
    val onScheduleSessionDetailsPage: Pair<Long, Long>,
    val afterScheduleSessionDetailsPage: Pair<Long, Long>,
    val onReviewYourSessionDetailsPage: Pair<Long, Long>,


    )
