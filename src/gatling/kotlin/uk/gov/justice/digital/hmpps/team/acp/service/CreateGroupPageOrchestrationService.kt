package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupSimulationSession
import uk.gov.justice.digital.hmpps.team.acp.helper.AcpSelectorHelper
import uk.gov.justice.digital.hmpps.team.acp.constants.*

class CreateGroupPageOrchestrationService(
    private val acpSelectorHelper: AcpSelectorHelper = AcpSelectorHelper()
) {

    fun getCreateGroupPageAndDoChecks() =
        HttpDsl.http("GET - Create group Page")
            .get("/create-group")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Create a group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postCreateGroupPageAndDoChecks() =
        HttpDsl.http("POST - Create group Page")
            .post { "/create-group" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getCreateGroupCodePageAndDoChecks() =
        HttpDsl.http("GET - Create a group code Page")
            .get("/create-group-code")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Create a group code"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postCreateGroupCodePageAndDoChecks() =
        HttpDsl.http("POST - Create a group code Page")
            .post { "/create-group-code" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-code", "#{groupCode}")
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupStartDatePageAndDoChecks() =
        HttpDsl.http("GET - Add a start date for the group Page")
            .get("/group-start-date")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Add a start date for the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postGroupStartDatePageAndDoChecks() =
        HttpDsl.http("POST - Add a start date for the group Page")
            .post { "/group-start-date" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-date", CREATE_GROUP_DATE)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupDaysAndTimesPageAndDoChecks() =
        HttpDsl.http("GET - When will the group run? Page")
            .get("/group-days-and-times")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("When will the group run?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postGroupDaysAndTimesPageAndDoChecks() =
        HttpDsl.http("POST - When will the group run? Page")
            .post { "/group-days-and-times" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("days-of-week", DAYS_OF_WEEK_MONDAY)
            .formParam("monday-hour", MONDAY_HOUR_ONE)
            .formParam("monday-ampm", MONDAY_AMPM_PM)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupCohortPageAndDoChecks() =
        HttpDsl.http("GET - Select the group cohort Page")
            .get("/group-cohort")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Select the group cohort"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postGroupCohortPageAndDoChecks() =
        HttpDsl.http("POST - Select the group cohort Page")
            .post { "/group-cohort" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-cohort", GROUP_COHORT)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupGenderPageAndDoChecks() =
        HttpDsl.http("GET - Select the gender of the group Page")
            .get("/group-gender")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Select the gender of the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postGroupGenderPageAndDoChecks() =
        HttpDsl.http("POST - Select the gender of the group Page")
            .post { "/group-gender" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-sex", GROUP_SEX)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getProbationDeliveryUnitPageAndDoChecks() =
        HttpDsl.http("GET - In which probation delivery unit (PDU) will the group take place? Page")
            .get("/group-probation-delivery-unit")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("In which probation delivery unit (PDU) will the group take place?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postProbationDeliveryUnitPageAndDoChecks() =
        HttpDsl.http("POST - In which probation delivery unit (PDU) will the group take place? Page")
            .post { "/group-probation-delivery-unit" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-pdu", GROUP_PDU)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getDeliveryLocationPageAndDoChecks() =
        HttpDsl.http("GET - Where will the group take place?")
            .get("/group-delivery-location")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Where will the group take place?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postDeliveryLocationPageAndDoChecks() =
        HttpDsl.http("POST - Where will the group take place? Page")
            .post { "/group-delivery-location" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-location", GROUP_LOCATION)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupFacilitatorsPageAndDoChecks() =
        HttpDsl.http("GET - Who is responsible for the group?")
            .get("/group-facilitators")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Who is responsible for the group?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postGroupFacilitatorsPageAndDoChecks() =
        HttpDsl.http("POST - Who is responsible for the group? Page")
            .post { "/group-facilitators" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-treatment-manager", GROUP_TREATMENT_MANAGER)
            .formParam("create-group-facilitator", GROUP_FACILITATOR)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupReviewDetailsPageAndDoChecks() =
        HttpDsl.http("GET - Review your group details Page")
            .get("/group-review-details")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Review your group details"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postGroupReviewDetailsPageAndDoChecks() =
        HttpDsl.http("POST - Review your group details Page")
            .post { "/group-review-details" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getGroupCreatedPageAndDoChecks() =
        HttpDsl.http("GET - group created Page")
            .get{ session ->
        val groupId = session.getString(CreateGroupSimulationSession.GROUP_ID.sessionKey)
        val groupCode = session.getString(CreateGroupSimulationSession.GROUP_CODE.sessionKey)
        "/group/$groupId/schedule-overview?message=Group $groupCode created"}
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("div.moj-alert__content")
                    .find()
                    .`is` { session ->
                        val groupCode = session.getString(CreateGroupSimulationSession.GROUP_CODE.sessionKey)
                        "Group $groupCode created"
                    },
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )
}