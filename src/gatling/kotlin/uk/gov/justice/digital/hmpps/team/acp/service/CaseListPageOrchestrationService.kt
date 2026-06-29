package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.team.acp.model.CaseListSimulationSession

class CaseListPageOrchestrationService (
) {
    fun hitOpenReferralsPageAndDoChecks() =
        HttpDsl.http("Open referrals Page")
            .get("/region/open-referrals")
            .check(HttpDsl.status().`is` { 200 },
             CoreDsl.css("h1").find().`is`("Case list"))

    fun hitReferralDetailsPageAndDoChecks() =
        HttpDsl.http("Referral details Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                    "/referral-details/$referralId/personal-details"}
            .check(HttpDsl.status().`is` { 200 },
                CoreDsl.css("h1:contains('Referral details')").exists()
            )

    fun hitRisksAndNeedsPageAndDoChecks() =
        HttpDsl.http("Risks and needs Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/risks-and-needs/risks-and-alerts"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Risks and needs')").exists()
            )

    fun hitProgrammeNeedsIdentifierPageAndDoChecks() =
        HttpDsl.http("Programme needs identifier Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/programme-needs-identifier"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Programme needs identifier')").exists()
            )

    fun hitAvailabilityAndMotivationPageAndDoChecks() =
        HttpDsl.http("Availability and motivation Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/availability-and-motivation/availability"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Availability and motivation')").exists()
            )

    fun hitAttendanceHistoryPageAndDoChecks() =
        HttpDsl.http("Attendance history Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/attendance-history"}
            .check(HttpDsl.status().`is` { 200 },
            CoreDsl.css("h1:contains('Attendance history')").exists()
            )

    fun hitStatusHistoryPageAndDoChecks() =
        HttpDsl.http("Status history Page")
            .get{ session ->
                val referralId = session.getString(CaseListSimulationSession.REFERRAL_ID.sessionKey)
                "/referral/$referralId/status-history"}
            .check(HttpDsl.status().`is` { 200 },
             CoreDsl.css("h1:contains('Status history')").exists()

            )

    //referral/4c68324c-6674-4705-9b91-d3b956261ce1/update-status-scheduled

}