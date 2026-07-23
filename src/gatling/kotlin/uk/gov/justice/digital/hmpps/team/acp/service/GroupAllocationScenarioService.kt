package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.jdbc.GroupAllocationFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.GroupAllocationPauseConfig
import java.time.Duration

class GroupAllocationScenarioService(
    private val groupAllocationFeeder: GroupAllocationFeeder = GroupAllocationFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val signInService: SignInService = SignInService(),
    private val pageOrchestrationService: GroupAllocationPageOrchestrationService = GroupAllocationPageOrchestrationService(),
) {
    fun buildScenario(
        scenarioName: String,
        pauses: GroupAllocationPauseConfig,
        journeyDuration: Duration,
    ): ScenarioBuilder {
        // Authenticate via HMPPS-Auth
        val authenticate =
            httpRequestHelper.acpAuthCookie
                ?.let { CoreDsl.exec(HttpDsl.addCookie(it)) }
                ?: signInService.signIn()

        val singleGroupAllocationJourney =
            CoreDsl
                .feed(groupAllocationFeeder.getJdbcFeederForGroupAllocation())
                .pause(pauses.beforeStart.first, pauses.beforeStart.second)
                .exec(pageOrchestrationService.getWaitlistPageAndCheckPersonPresent())
                .pause(pauses.onWaitlistPage.first, pauses.onWaitlistPage.second)
                .exec(pageOrchestrationService.postWaitlistPageSelectPerson())
                .pause(pauses.afterWaitlistPage.first, pauses.afterWaitlistPage.second)
                .exec(pageOrchestrationService.getAddToGroupConfirmPage())
                .pause(pauses.onAddToGroupConfirmPage.first, pauses.onAddToGroupConfirmPage.second)
                .exec(pageOrchestrationService.postAddToGroupConfirmYes())
                .pause(pauses.afterAddToGroupConfirmPage.first, pauses.afterAddToGroupConfirmPage.second)
                .exec(pageOrchestrationService.getScheduledStatusDetailsPage())
                .pause(pauses.onScheduledStatusDetailsPage.first, pauses.onScheduledStatusDetailsPage.second)
                .exec(pageOrchestrationService.postScheduledStatusDetails())
                .pause(pauses.afterScheduledStatusDetailsPage.first, pauses.afterScheduledStatusDetailsPage.second)
                .exec(pageOrchestrationService.getAllocatedListPageAndCheckPersonPresent())
                .pause(pauses.onAllocatedListPage.first, pauses.onAllocatedListPage.second)
                .exec(pageOrchestrationService.getWaitlistPageAndCheckPersonAbsent())
                .pause(pauses.afterAllocatedListPage.first, pauses.afterAllocatedListPage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .exec(
                CoreDsl.exitBlockOnFail().on(singleGroupAllocationJourney),
            )
    }
}
