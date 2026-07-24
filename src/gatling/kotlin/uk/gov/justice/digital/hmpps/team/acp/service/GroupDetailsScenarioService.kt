package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.helper.GroupCodeGenerator
import uk.gov.justice.digital.hmpps.team.acp.jdbc.GroupDetailsFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.GroupDetailsPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.model.GroupDetailsSimulationSession
import java.time.Duration

class GroupDetailsScenarioService(
    private val groupDetailsFeeder: GroupDetailsFeeder = GroupDetailsFeeder(),
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val signInService: SignInService = SignInService(),
    private val pageOrchestrationService: GroupDetailsPageOrchestrationService = GroupDetailsPageOrchestrationService(),
) {
    fun buildScenario(
        scenarioName: String,
        pauses: GroupDetailsPauseConfig,
        journeyDuration: Duration,
    ): ScenarioBuilder {
        // Authenticate via HMPPS-Auth
        val authenticate =
            httpRequestHelper.acpAuthCookie
                ?.let { CoreDsl.exec(HttpDsl.addCookie(it)) }
                ?: signInService.signIn()

        val singleGroupDetailsJourney =
            CoreDsl
                .feed(groupDetailsFeeder.getJdbcFeederForGroup())
                .pause(pauses.beforeStart.first, pauses.beforeStart.second)
                .exec(pageOrchestrationService.getGroupDetailsPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group code
                .exec(pageOrchestrationService.getEditGroupCodePageAndDoChecks())
                .pause(pauses.onEditGroupCodePage.first, pauses.onEditGroupCodePage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupCodePageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                //Edit group start date
                .exec(pageOrchestrationService.getEditGroupStartDatePageAndDoChecks())
                .pause(pauses.onEditGroupStartDatePage.first, pauses.onEditGroupStartDatePage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupStartDatePageAndDoChecks())
                .pause(pauses.afterEditGroupStartDatePage.first, pauses.afterEditGroupStartDatePage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupStartDateReschedulePageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group days and times
                .exec(pageOrchestrationService.getEditGroupDaysAndTimesPageAndDoChecks())
                .pause(pauses.onEditGroupDaysAndTimesPage.first, pauses.onEditGroupDaysAndTimesPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupDaysAndTimesPageAndDoChecks())
                .pause(pauses.afterEditGroupDaysAndTimesPage.first, pauses.afterEditGroupDaysAndTimesPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupDaysAndTimesReschedulePageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group cohort
                .exec(pageOrchestrationService.getEditGroupCohortPageAndDoChecks())
                .pause(pauses.onEditGroupCohortPage.first, pauses.onEditGroupCohortPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupCohortPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group gender
                .exec(pageOrchestrationService.getEditGroupGenderPageAndDoChecks())
                .pause(pauses.onEditGroupGenderPage.first, pauses.onEditGroupGenderPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupGenderPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group probation delivery unit
                .exec(pageOrchestrationService.getEditGroupProbationDeliveryUnitPageAndDoChecks())
                .pause(pauses.onEditGroupProbationDeliveryUnitPage.first, pauses.onEditGroupProbationDeliveryUnitPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupProbationDeliveryUnitPageAndDoChecks())
                .pause(pauses.afterEditGroupProbationDeliveryUnitPage.first, pauses.afterEditGroupProbationDeliveryUnitPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupDeliveryLocationPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group delivery location
                .exec(pageOrchestrationService.getEditGroupDeliveryLocationPageAndDoChecks())
                .pause(pauses.onEditGroupDeliveryLocationPage.first, pauses.onEditGroupDeliveryLocationPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupDeliveryLocationPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group treatment manager
                .exec(pageOrchestrationService.getEditGroupFacilitatorPageAndDoChecks())
                .pause(pauses.onEditGroupFacilitatorPage.first, pauses.onEditGroupFacilitatorPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupFacilitatorPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group facilitators
                .exec(pageOrchestrationService.getEditGroupFacilitatorPageAndDoChecks())
                .pause(pauses.onEditGroupFacilitatorPage.first, pauses.onEditGroupFacilitatorPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupFacilitatorPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)
                // Edit group cover facilitators
                .exec(pageOrchestrationService.getEditGroupFacilitatorPageAndDoChecks())
                .pause(pauses.onEditGroupFacilitatorPage.first, pauses.onEditGroupFacilitatorPage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postEditGroupFacilitatorPageAndDoChecks())
                .pause(pauses.onGroupDetailsPage.first, pauses.onGroupDetailsPage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .during(journeyDuration)
            .on(
                CoreDsl.exitBlockOnFail().on(singleGroupDetailsJourney),
            )
    }
}
