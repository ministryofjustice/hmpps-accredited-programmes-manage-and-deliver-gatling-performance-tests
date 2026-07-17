package uk.gov.justice.digital.hmpps.team.acp.model

enum class CaseListSimulationSession(
    val sessionKey: String,
) {
    REFERRAL_ID("referralid"),
    REFERRAL_NAME("referralname"),
    CSRF_TOKEN_VALUE("_csrf"),
}
