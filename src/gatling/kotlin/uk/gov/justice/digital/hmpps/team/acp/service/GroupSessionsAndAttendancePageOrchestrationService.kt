package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CheckBuilder
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.constants.DAYS_OF_WEEK_TUESDAY
import uk.gov.justice.digital.hmpps.team.acp.constants.EDIT_GROUP_COHORT
import uk.gov.justice.digital.hmpps.team.acp.constants.EDIT_GROUP_FACILITATOR
import uk.gov.justice.digital.hmpps.team.acp.constants.EDIT_GROUP_LOCATION
import uk.gov.justice.digital.hmpps.team.acp.constants.EDIT_GROUP_PDU
import uk.gov.justice.digital.hmpps.team.acp.constants.EDIT_GROUP_SEX
import uk.gov.justice.digital.hmpps.team.acp.constants.EDIT_GROUP_TREATMENT_MANAGER
import uk.gov.justice.digital.hmpps.team.acp.constants.RESCHEDULE_OTHER_SESSIONS
import uk.gov.justice.digital.hmpps.team.acp.constants.TUESDAY_AMPM_PM
import uk.gov.justice.digital.hmpps.team.acp.constants.TUESDAY_HOUR_ONE
import uk.gov.justice.digital.hmpps.team.acp.constants.generateCreateGroupDate
import uk.gov.justice.digital.hmpps.team.acp.helper.AcpSelectorHelper
import uk.gov.justice.digital.hmpps.team.acp.model.GroupDetailsSimulationSession
import uk.gov.justice.digital.hmpps.team.acp.model.GroupSessionsAndAttendanceSimulationSession
import uk.gov.justice.digital.hmpps.team.acp.model.GroupSessionsAndAttendanceSimulationSession.EXTRACTED_SCHEDULE_PRE_GROUP_SESSION_HREF
import uk.gov.justice.digital.hmpps.team.acp.model.GroupSessionsAndAttendanceSimulationSession.GROUP_ID

class GroupSessionsAndAttendancePageOrchestrationService(
    private val acpSelectorHelper: AcpSelectorHelper = AcpSelectorHelper(),
) {
    private fun redirectedTo(path: String): CheckBuilder =
        HttpDsl
            .currentLocationRegex("${Regex.escape(path)}(\\?.*)?$")
            .find()
            .exists()

    fun getGroupSessionsAndAttendancePageAndDoChecks() =
        HttpDsl
            .http("GET - Group sessions and attendance Page")
            .get { session ->
                val groupId = session.getString(GROUP_ID.sessionKey)
                "/group/$groupId/sessions-and-attendance"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Sessions and attendance"),
                CoreDsl.css("a:contains('Schedule a pre-group session')", "href")
                    .saveAs(EXTRACTED_SCHEDULE_PRE_GROUP_SESSION_HREF.sessionKey),
                )

    fun getSchedulePreGroupOneToOneSessionTypePageAndDoChecks() =
        HttpDsl
            .http("GET - Schedule pre-group one-to-one session type Page")
            .get { session ->
                val href = session.getString(EXTRACTED_SCHEDULE_PRE_GROUP_SESSION_HREF.sessionKey)
                href
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Which session are you scheduling?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupCodePageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group code Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-code"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-code", "#{${GroupDetailsSimulationSession.GROUP_CODE.sessionKey}}")
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )

    fun getEditGroupStartDatePageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group start date Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-start-date"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit start date for the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupStartDatePageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group start date Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-start-date"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-date", generateCreateGroupDate())
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/edit-start-date-rescheduled"),
            )

    fun postEditGroupStartDateReschedulePageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group start date reschedule Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-start-date-rescheduled"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("reschedule-other-sessions", RESCHEDULE_OTHER_SESSIONS)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )

    fun getEditGroupDaysAndTimesPageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group days and times Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-days-and-times"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit when will the group run"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupDaysAndTimesPageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group days and times Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-days-and-times"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("days-of-week", DAYS_OF_WEEK_TUESDAY)
            .formParam("tuesday-hour", TUESDAY_HOUR_ONE)
            .formParam("tuesday-ampm", TUESDAY_AMPM_PM)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/edit-group-days-and-times/reschedule"),
            )

    fun postEditGroupDaysAndTimesReschedulePageAndDoChecks() =
        HttpDsl
            .http("POST - Edit group days and times reschedule Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-days-and-times/reschedule"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("reschedule-other-sessions", RESCHEDULE_OTHER_SESSIONS)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )

    fun getEditGroupCohortPageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group cohort Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-cohort"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit the group cohort"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupCohortPageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group cohort Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-cohort"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-cohort", EDIT_GROUP_COHORT)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )

    fun getEditGroupGenderPageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group gender Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-gender"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit the gender of the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupGenderPageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group gender Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-gender"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-sex", EDIT_GROUP_SEX)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )

    fun getEditGroupProbationDeliveryUnitPageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group probation delivery unit Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-probation-delivery-unit"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit the probation delivery unit (PDU) where the group will take place"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupProbationDeliveryUnitPageAndDoChecks() =
        HttpDsl
            .http("POST - Edit a group probation delivery unit Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-probation-delivery-unit"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-pdu", EDIT_GROUP_PDU)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/edit-group-delivery-location"),
            )

    fun postEditGroupDeliveryLocationPageAndDoChecks() =
        HttpDsl
            .http("POST - Edit group delivery location Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-delivery-location"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-location", EDIT_GROUP_LOCATION)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )

    fun getEditGroupDeliveryLocationPageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group delivery location Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-delivery-location"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit where the group will take place"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun getEditGroupFacilitatorPageAndDoChecks() =
        HttpDsl
            .http("GET - Edit group facilitator Page")
            .get { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-facilitators"
            }.check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Edit who is responsible for the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postEditGroupFacilitatorPageAndDoChecks() =
        HttpDsl
            .http("POST - Edit group facilitator Page")
            .post { session ->
                val groupId = session.getString(GroupDetailsSimulationSession.GROUP_ID.sessionKey)
                "/$groupId/edit-group-facilitators"
            }.formParam("_csrf", "#{${GroupDetailsSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-treatment-manager", EDIT_GROUP_TREATMENT_MANAGER)
            .formParam("create-group-facilitator", EDIT_GROUP_FACILITATOR)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{${GroupDetailsSimulationSession.GROUP_ID.sessionKey}}/group-details"),
            )
}
