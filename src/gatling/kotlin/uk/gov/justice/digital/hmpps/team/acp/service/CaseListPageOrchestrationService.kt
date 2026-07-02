package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListSimulationSession
import uk.gov.justice.digital.hmpps.team.acp.constants.*
import uk.gov.justice.digital.hmpps.team.acp.helper.AcpSelectorHelper


class CaseListPageOrchestrationService (
    private val acpSelectorHelper: AcpSelectorHelper = AcpSelectorHelper()
) {

    fun getOpenReferralsPageAndDoChecks() =
        HttpDsl.http("GET - Open referrals Page")
            .get("/region/open-referrals")
            .check(HttpDsl.status().`is` { 200 },
             CoreDsl.css("h1").find().`is`("Case list"))

    fun getReferralDetailsPageAndDoChecks() =
        HttpDsl.http("GET - Referral details Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                    "/referral-details/$referralId/personal-details"}
            .check(HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Referral details')").exists()
            )

    fun getRisksAndNeedsPageAndDoChecks() =
        HttpDsl.http("GET - Risks and needs Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/risks-and-needs/risks-and-alerts"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Risks and needs')").exists()
            )

    fun getProgrammeNeedsIdentifierPageAndDoChecks() =
        HttpDsl.http("GET - Programme needs identifier Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/programme-needs-identifier"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Programme needs identifier')").exists()
            )

    fun getAvailabilityAndMotivationPageAndDoChecks() =
        HttpDsl.http("GET - Availability and motivation Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/availability-and-motivation/availability"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Availability and motivation')").exists()
            )

    fun getAttendanceHistoryPageAndDoChecks() =
        HttpDsl.http("GET - Attendance history Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/attendance-history"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Attendance history')").exists()
            )

    fun getStatusHistoryPageAndDoChecks() =
        HttpDsl.http("GET - Status history Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/status-history"}
            .check(HttpDsl.status().`is` { 200 },
             CoreDsl.css("h1:contains('Status history')").exists()
            )

    fun getUpdateLearningDisabilitiesAndChallengesPageAndDoChecks() =
        HttpDsl.http("GET - Update learning disabilities and challenges (LDC) status Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/update-learning-disabilities-and-challenges"}
            .check(HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Update learning disabilities and challenges (LDC) status')").exists(),
                acpSelectorHelper.getCsrfHiddenFieldValue(CaseListSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postUpdateLearningDisabilitiesAndChallengesPageAndDoChecks() =
        HttpDsl.http("POST - Update learning disabilities and challenges (LDC) status Page")
            .post{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/update-learning-disabilities-and-challenges"}
            .header("content-type", "application/x-www-form-urlencoded")
            .formParam("_csrf", "#{${CaseListSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("hasLdc", ldcFlag_MayNeedAnLDCAdaptedProgramme)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getReferralDetailsWithLdcUpdatedPageAndDoChecks() =
        HttpDsl.http("GET - Referral details when LDC updated Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral-details/$referralId/personal-details?isLdcUpdated=true"}
            .check(HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Referral details')").exists(),
            )

    fun getChangeCohortPageAndDoChecks() =
        HttpDsl.http("GET - Change cohort Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/change-cohort"}
            .check(HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Which cohort should')").exists(),
                acpSelectorHelper.getCsrfHiddenFieldValue(CaseListSimulationSession.CSRF_TOKEN_VALUE.sessionKey)
            )

    fun postChangeCohortPageAndDoChecks() =
        HttpDsl.http("POST - Change cohort Page")
            .post{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/change-cohort"}
            .header("content-type", "application/x-www-form-urlencoded")
            .formParam("_csrf", "#{${CaseListSimulationSession.CSRF_TOKEN_VALUE.sessionKey}}")
            .formParam("updatedCohort", changeCohort_GeneralOffence)
            .check(HttpDsl.status().`is` { 200 }
            )

    fun getReferralDetailsWithCohortUpdatedPageAndDoChecks() =
        HttpDsl.http("GET - Referral details when cohort updated Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral-details/$referralId/personal-details?isCohortUpdated=true"}
            .check(HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Referral details')").exists(),
            )

    fun getUpdateReferralStatusPageAndDoChecks() =
         HttpDsl.http("GET - Update Referral Status Page")
             .get{ session ->
                 val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                 "/referral/$referralId/update-status"}
             .check(HttpDsl.status().`is` { 200 },
                 CoreDsl.css("h1:contains('referral status')").exists()
             )

}