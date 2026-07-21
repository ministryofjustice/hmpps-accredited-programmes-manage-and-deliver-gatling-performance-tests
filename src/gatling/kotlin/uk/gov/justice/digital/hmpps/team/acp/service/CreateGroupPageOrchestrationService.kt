package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CheckBuilder
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.constants.CREATE_GROUP_DATE
import uk.gov.justice.digital.hmpps.team.acp.constants.DAYS_OF_WEEK_MONDAY
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_COHORT
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_FACILITATOR
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_LOCATION
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_PDU
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_SEX
import uk.gov.justice.digital.hmpps.team.acp.constants.GROUP_TREATMENT_MANAGER
import uk.gov.justice.digital.hmpps.team.acp.constants.MONDAY_AMPM_PM
import uk.gov.justice.digital.hmpps.team.acp.constants.MONDAY_HOUR_ONE
import uk.gov.justice.digital.hmpps.team.acp.helper.AcpSelectorHelper
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupSimulationSession

class CreateGroupPageOrchestrationService(
    private val acpSelectorHelper: AcpSelectorHelper = AcpSelectorHelper(),
) {
    private fun redirectedTo(path: String): CheckBuilder =
        HttpDsl
            .currentLocationRegex("${Regex.escape(path)}(\\?.*)?$")
            .find()
            .exists()

    fun getCreateGroupPageAndDoChecks() =
        HttpDsl
            .http("GET - Create group Page")
            .get("/create-group")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Create a group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postCreateGroupPageAndDoChecks() =
        HttpDsl
            .http("POST - Create group Page")
            .post { "/create-group" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/create-group-code"),
            )

    fun getCreateGroupCodePageAndDoChecks() =
        HttpDsl
            .http("GET - Create a group code Page")
            .get("/create-group-code")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Create a group code"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postCreateGroupCodePageAndDoChecks() =
        HttpDsl
            .http("POST - Create a group code Page")
            .post { "/create-group-code" }
.formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-code", "#{${CreateGroupSimulationSession.GROUP_CODE.sessionKey}}")
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-start-date"),
            )

    fun getGroupStartDatePageAndDoChecks() =
        HttpDsl
            .http("GET - Add a start date for the group Page")
            .get("/group-start-date")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Add a start date for the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postGroupStartDatePageAndDoChecks() =
        HttpDsl
            .http("POST - Add a start date for the group Page")
            .post { "/group-start-date" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-date", CREATE_GROUP_DATE)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-days-and-times"),
            )

    fun getGroupDaysAndTimesPageAndDoChecks() =
        HttpDsl
            .http("GET - When will the group run? Page")
            .get("/group-days-and-times")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("When will the group run?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postGroupDaysAndTimesPageAndDoChecks() =
        HttpDsl
            .http("POST - When will the group run? Page")
            .post { "/group-days-and-times" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("days-of-week", DAYS_OF_WEEK_MONDAY)
            .formParam("monday-hour", MONDAY_HOUR_ONE)
            .formParam("monday-ampm", MONDAY_AMPM_PM)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-cohort"),
            )

    fun getGroupCohortPageAndDoChecks() =
        HttpDsl
            .http("GET - Select the group cohort Page")
            .get("/group-cohort")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Select the group cohort"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postGroupCohortPageAndDoChecks() =
        HttpDsl
            .http("POST - Select the group cohort Page")
            .post { "/group-cohort" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-cohort", GROUP_COHORT)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-gender"),
            )

    fun getGroupGenderPageAndDoChecks() =
        HttpDsl
            .http("GET - Select the gender of the group Page")
            .get("/group-gender")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Select the gender of the group"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postGroupGenderPageAndDoChecks() =
        HttpDsl
            .http("POST - Select the gender of the group Page")
            .post { "/group-gender" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-sex", GROUP_SEX)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-probation-delivery-unit"),
            )

    fun getProbationDeliveryUnitPageAndDoChecks() =
        HttpDsl
            .http("GET - In which probation delivery unit (PDU) will the group take place? Page")
            .get("/group-probation-delivery-unit")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("In which probation delivery unit (PDU) will the group take place?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postProbationDeliveryUnitPageAndDoChecks() =
        HttpDsl
            .http("POST - In which probation delivery unit (PDU) will the group take place? Page")
            .post { "/group-probation-delivery-unit" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-pdu", GROUP_PDU)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-delivery-location"),
            )

    fun getDeliveryLocationPageAndDoChecks() =
        HttpDsl
            .http("GET - Where will the group take place?")
            .get("/group-delivery-location")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Where will the group take place?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postDeliveryLocationPageAndDoChecks() =
        HttpDsl
            .http("POST - Where will the group take place? Page")
            .post { "/group-delivery-location" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-location", GROUP_LOCATION)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-facilitators"),
            )

    fun getGroupFacilitatorsPageAndDoChecks() =
        HttpDsl
            .http("GET - Who is responsible for the group?")
            .get("/group-facilitators")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Who is responsible for the group?"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    fun postGroupFacilitatorsPageAndDoChecks() =
        HttpDsl
            .http("POST - Who is responsible for the group? Page")
            .post { "/group-facilitators" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("create-group-treatment-manager", GROUP_TREATMENT_MANAGER)
            .formParam("create-group-facilitator", GROUP_FACILITATOR)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group-review-details"),
            )

    fun getGroupReviewDetailsPageAndDoChecks() =
        HttpDsl
            .http("GET - Review your group details Page")
            .get("/group-review-details")
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`("Review your group details"),
                acpSelectorHelper.getCsrfHiddenFieldValue(CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey),
            )

    // Submitting the review page creates the group and redirects to
    // /group/{id}/schedule-overview — the redirect is where the new group's id comes from,
    // and the landing page carries the "Group <code> created" banner.
    fun postGroupReviewDetailsPageAndDoChecks() =
        HttpDsl
            .http("POST - Review your group details Page")
            .post { "/group-review-details" }
            .formParam("_csrf", "#{${CreateGroupSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .check(
                HttpDsl.status().`is` { 200 },
                HttpDsl
                    .currentLocationRegex("/group/([^/]+)/schedule-overview")
                    .find()
                    .saveAs(CreateGroupSimulationSession.GROUP_ID.sessionKey),
                CoreDsl
                    .css("div.moj-alert__content")
                    .find()
                    .`is` { session ->
                        val groupCode = session.getString(CreateGroupSimulationSession.GROUP_CODE.sessionKey)
                        "Group $groupCode created"
                    },
            )
}
