package uk.gov.justice.digital.hmpps.team.acp.model

enum class GroupAllocationSimulationSession(
    val sessionKey: String,
) {
    GROUP_ID("groupid"),
    REFERRAL_ID("referralid"),
    CSRF_TOKEN_VALUE("_csrf"),
}
