
enum class CaseListSimulationSession(val sessionKey: String) {
    REFERRAL_ID("referralid"),
    REFERRAL_NAME("referralname"),
    CSRF_TOKEN_VALUE ("_csrf"),
}

enum class CreateGroupSimulationSession(val sessionKey: String) {
    GROUP_ID("groupId"),
    GROUP_CODE("groupCode"),
    CSRF_TOKEN_VALUE ("_csrf"),
}
