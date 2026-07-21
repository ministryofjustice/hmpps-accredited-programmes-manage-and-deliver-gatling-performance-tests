package uk.gov.justice.digital.hmpps.team.acp.model

enum class CreateGroupSimulationSession(
    val sessionKey: String,
) {
    GROUP_ID("groupId"),
    GROUP_CODE("groupCode"),
    CSRF_TOKEN_VALUE("_csrf"),
}
