package uk.gov.justice.digital.hmpps.team.acp.model

enum class GroupDetailsSimulationSession(
    val sessionKey: String,
) {
    GROUP_ID("groupid"),
    GROUP_CODE("groupcode"),
    CSRF_TOKEN_VALUE("_csrf"),
}
