package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CheckBuilder
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.helper.PreGroupOneToOneSelectors
import uk.gov.justice.digital.hmpps.team.acp.model.PreGroupOneToOneSimulationSession
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_ID
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_DATE
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_START_TIME_HOUR
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_START_TIME_PART_OF_DAY
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_END_TIME_HOUR
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_END_TIME_PART_OF_DAY
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_DETAILS_WHO
import uk.gov.justice.digital.hmpps.team.acp.constants.PRE_GROUP_ONE_TO_ONE_SESSION_DETAILS_FACILITATOR


import uk.gov.justice.digital.hmpps.team.acp.helper.AcpSelectorHelper


class PreGroupOneToOnePageOrchestrationService(
    private val acpSelectorHelper: AcpSelectorHelper = AcpSelectorHelper()
) {
    private val preGroupOneToOneSessionId: String = PRE_GROUP_ONE_TO_ONE_SESSION_ID

    private fun redirectedTo(path: String): CheckBuilder =
        HttpDsl
            .currentLocationRegex("${Regex.escape(path)}(\\?.*)?$")
            .find()
            .exists()

    fun getSessionAndAttendancePageAndDoChecks() =
        HttpDsl
            .http("GET - Sessions and attendance Page")
            .get { session ->
                val groupId = session.getString(PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey)
                "/group/$groupId/sessions-and-attendance"
            }.check(
                HttpDsl.status().`is` { 200 },
                PreGroupOneToOneSelectors.headingCheck(),
            )

    fun getScheduleSessionTypePageAndDoChecks() =
    HttpDsl
        .http("GET - Which session are you scheduling? Page")
        .get { session ->
            val groupId = session.getString(PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey)
             "/$groupId/$preGroupOneToOneSessionId/schedule-session-type"
        }.check(
            HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Which session are you scheduling?')").exists(),
            acpSelectorHelper.getCsrfHiddenFieldValue(PreGroupOneToOneSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
        )

    fun postScheduleSessionTypePageAndDoChecks() =
        HttpDsl
            .http("POST - Which session are you scheduling? Page")
            .post { session ->
                val groupId = session.getString(PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/$preGroupOneToOneSessionId/schedule-session-type"
            }.formParam("_csrf", "#{${PreGroupOneToOneSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("session-template", PRE_GROUP_ONE_TO_ONE_SESSION)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("#{${PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey}}/$preGroupOneToOneSessionId/schedule-session-details")
            )

    fun getScheduleSessionDetailsPageAndDoChecks() =
        HttpDsl
            .http("GET - Add session details Page")
            .get { session ->
                 val groupId = session.getString(PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey)
                 "/$groupId/$preGroupOneToOneSessionId/schedule-session-details"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Add session details')").exists(),
                acpSelectorHelper.getCsrfHiddenFieldValue(PreGroupOneToOneSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postScheduleSessionDetailsPageAndDoChecks() =
        HttpDsl
            .http("POST - Add session details Page")
            .post { session ->
                val groupId = session.getString(PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/$preGroupOneToOneSessionId/schedule-session-details"
            }.formParam("_csrf", "#{${PreGroupOneToOneSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("session-details-date", PRE_GROUP_ONE_TO_ONE_SESSION_DATE)
            .formParam("session-details-start-time-hour", PRE_GROUP_ONE_TO_ONE_SESSION_START_TIME_HOUR)
            .formParam("session-details-start-time-part-of-day", PRE_GROUP_ONE_TO_ONE_SESSION_START_TIME_PART_OF_DAY)
            .formParam("session-details-end-time-hour", PRE_GROUP_ONE_TO_ONE_SESSION_END_TIME_HOUR)
            .formParam("session-details-end-time-part-of-day", PRE_GROUP_ONE_TO_ONE_SESSION_END_TIME_PART_OF_DAY)
            .formParam("session-details-who", PRE_GROUP_ONE_TO_ONE_SESSION_DETAILS_WHO)
            .formParam("session-details-facilitator-0", PRE_GROUP_ONE_TO_ONE_SESSION_DETAILS_FACILITATOR)
            .check(
                HttpDsl.status().`is` { 200 },
                 redirectedTo("#{${PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey}}/$preGroupOneToOneSessionId/session-review-details")
            )
    fun getReviewYourSessionDetailsPageAndDoChecks() =
        HttpDsl
            .http("GET - Review your session details Page")
            .get { session ->
                val groupId = session.getString(PreGroupOneToOneSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/$preGroupOneToOneSessionId/session-review-details"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Review your session details')").exists(),
                acpSelectorHelper.getCsrfHiddenFieldValue(PreGroupOneToOneSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )
}

