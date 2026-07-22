package uk.gov.justice.digital.hmpps.team.acp.model

data class ScheduleOverviewPauseConfig(
    val beforeStart: Pair<Long, Long>,
    val onSchedulePage: Pair<Long, Long>,
)
