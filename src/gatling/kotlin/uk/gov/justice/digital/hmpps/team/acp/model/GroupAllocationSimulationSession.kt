package uk.gov.justice.digital.hmpps.team.acp.model

enum class GroupAllocationSimulationSession(
    val sessionKey: String,
) {
    GROUP_ID("groupId"),
    REFERRAL_ID("referralId"),
    CSRF_TOKEN_VALUE("_csrf"),
}
