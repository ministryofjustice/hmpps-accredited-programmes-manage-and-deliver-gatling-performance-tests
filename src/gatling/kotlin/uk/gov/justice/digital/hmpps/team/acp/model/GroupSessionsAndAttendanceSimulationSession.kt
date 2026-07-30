package uk.gov.justice.digital.hmpps.team.acp.model

enum class GroupSessionsAndAttendanceSimulationSession(
    val sessionKey: String,
) {
    GROUP_ID("groupid"),
    GROUP_CODE("groupcode"),
    EXTRACTED_SCHEDULE_PRE_GROUP_SESSION_HREF("extractedschedulepregroupsessionhref"),
    CSRF_TOKEN_VALUE("_csrf"),
}
