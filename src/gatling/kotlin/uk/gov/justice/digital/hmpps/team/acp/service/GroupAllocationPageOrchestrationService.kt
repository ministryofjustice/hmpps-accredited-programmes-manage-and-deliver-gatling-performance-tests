package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CheckBuilder
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.constants.ADD_TO_GROUP_ADDITIONAL_DETAILS
import uk.gov.justice.digital.hmpps.team.acp.constants.ADD_TO_GROUP_CONFIRM_YES
import uk.gov.justice.digital.hmpps.team.acp.helper.AcpSelectorHelper
import uk.gov.justice.digital.hmpps.team.acp.model.GroupAllocationSimulationSession

class GroupAllocationPageOrchestrationService(
    private val acpSelectorHelper: AcpSelectorHelper = AcpSelectorHelper(),
) {
    private val groupId = GroupAllocationSimulationSession.GROUP_ID.sessionKey
    private val referralId = GroupAllocationSimulationSession.REFERRAL_ID.sessionKey
    private val csrf = GroupAllocationSimulationSession.CSRF_TOKEN_VALUE.sessionKey

    private val addToGroupHeadingPattern = Regex("^Add .+ to this group\\?$")
    private val scheduleStatusDetailsHeadingPattern = Regex(".+ referral status will change to Scheduled\\?$")

    private val allocationsAndWaitlistHeading = "Allocations and waitlist"

    private fun redirectedTo(path: String): CheckBuilder =
        HttpDsl
            .currentLocationRegex("$path(\\?.*)?$")
            .find()
            .exists()

    private fun checkWaitlistRowExists(): CheckBuilder = CoreDsl.css("input[name='add-to-group'][value='#{$referralId}']").exists()

    private fun checkWaitlistRowNotExists(): CheckBuilder = CoreDsl.css("input[name='add-to-group'][value='#{$referralId}']").notExists()

    fun getWaitlistPageAndCheckPersonPresent() =
        HttpDsl
            .http("GET - Group waitlist Page")
            .get { session -> "/group/${session.getString(groupId)}/waitlist" }
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`(allocationsAndWaitlistHeading),
                checkWaitlistRowExists(),
                acpSelectorHelper.getCsrfHiddenFieldValue(csrf),
            )

    fun postWaitlistPageSelectPerson() =
        HttpDsl
            .http("POST - Group waitlist Page (select person)")
            .post { session -> "/group/${session.getString(groupId)}/waitlist" }
            .formParam("_csrf", "#{$csrf}")
            .formParam("add-to-group", "#{$referralId}")
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/add-to-group/#{$groupId}/#{$referralId}"),
            )

    fun getAddToGroupConfirmPage() =
        HttpDsl
            .http("GET - Add to group confirm Page")
            .get { session -> "/add-to-group/${session.getString(groupId)}/${session.getString(referralId)}" }
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().validate("h1 matches 'Add <name> to this group?'") { heading, _ ->
                    if (heading != null && addToGroupHeadingPattern.matches(heading.trim())) {
                        heading
                    } else {
                        throw AssertionError("Unexpected add-to-group h1: $heading")
                    }
                },
                acpSelectorHelper.getCsrfHiddenFieldValue(csrf),
            )

    fun postAddToGroupConfirmYes() =
        HttpDsl
            .http("POST - Add to group confirm Page")
            .post { session -> "/add-to-group/${session.getString(groupId)}/${session.getString(referralId)}" }
            .formParam("_csrf", "#{$csrf}")
            .formParam("add-to-group", ADD_TO_GROUP_CONFIRM_YES)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/#{$groupId}/#{$referralId}/scheduled-status-details"),
            )

    fun getScheduledStatusDetailsPage() =
        HttpDsl
            .http("GET - Scheduled status details Page")
            .get { session -> "/${session.getString(groupId)}/${session.getString(referralId)}/scheduled-status-details" }
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl
                    .css("h1")
                    .find()
                    .validate("h1 matches '<name>'s referral status will change to scheduled'") { heading, _ ->
                        if (heading != null && scheduleStatusDetailsHeadingPattern.matches(heading.trim())) {
                            heading
                        } else {
                            throw AssertionError("Unexpected scheduled-status-details h1: $heading")
                        }
                    },
                acpSelectorHelper.getCsrfHiddenFieldValue(csrf),
            )

    fun postScheduledStatusDetails() =
        HttpDsl
            .http("POST - Scheduled status details Page")
            .post { session -> "/${session.getString(groupId)}/${session.getString(referralId)}/scheduled-status-details" }
            .formParam("_csrf", "#{$csrf}")
            .formParam("additional-details", ADD_TO_GROUP_ADDITIONAL_DETAILS)
            .check(
                HttpDsl.status().`is` { 200 },
                redirectedTo("/group/#{$groupId}/allocations"),
            )

    fun getAllocatedListPageAndCheckPersonPresent() =
        HttpDsl
            .http("GET - Group allocated list Page")
            .get { session -> "/group/${session.getString(groupId)}/allocations" }
            .check(
                HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1").find().`is`(allocationsAndWaitlistHeading),
                CoreDsl.substring("#{$referralId}").exists(),
            )

    fun getWaitlistPageAndCheckPersonAbsent() =
        HttpDsl
            .http("GET - Group waitlist Page (post-allocation)")
            .get { session -> "/group/${session.getString(groupId)}/waitlist" }
            .check(
                HttpDsl.status().`is` { 200 },
                checkWaitlistRowNotExists(),
            )
}
