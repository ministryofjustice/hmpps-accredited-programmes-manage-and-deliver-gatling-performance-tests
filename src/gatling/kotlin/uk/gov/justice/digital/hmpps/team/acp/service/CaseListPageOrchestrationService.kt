package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl

class CaseListPageOrchestrationService (
) {
    fun hitOpenReferralsPageAndDoChecks() =
        HttpDsl.http("Open Referrals Page")
            .get("/region/open-referrals")
            .check(HttpDsl.status().`is` { 200 },
             CoreDsl.css("h1").find().`is`("Case list"))
}