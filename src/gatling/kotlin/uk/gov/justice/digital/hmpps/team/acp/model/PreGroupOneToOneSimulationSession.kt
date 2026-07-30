package uk.gov.justice.digital.hmpps.team.acp.model

enum class PreGroupOneToOneSimulationSession(
    val sessionKey: String,
) {
    GROUP_ID("groupid"),
    CSRF_TOKEN_VALUE("_csrf"),
}
