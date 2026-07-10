package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupPauseConfig

class CreateGroupScenarioService(
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val pageOrchestrationService: CreateGroupPageOrchestrationService = CreateGroupPageOrchestrationService()
) {

    fun buildScenario(
        scenarioName: String,
        pauses: CreateGroupPauseConfig

    ): ScenarioBuilder {
        val cookie = checkNotNull(httpRequestHelper.acpAuthCookie) {
            "acpAuthCookie is null — did you pass -Dhmpps-accredited-programmes-manage-and-deliver-ui.session=<session-cookie>?"
        }

        val caseListChainBuilder = CoreDsl.exec(HttpDsl.addCookie(cookie))
            .pause(pauses.beforeStart.first, pauses.beforeStart.second)
            .exec(
                pageOrchestrationService.getCreateGroupPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onCreateGroupPage.first, pauses.onCreateGroupPage.second)
            .exec(
                pageOrchestrationService.postCreateGroupPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterCreateGroupPage.first, pauses.afterCreateGroupPage.second)
            .exec(
                pageOrchestrationService.getCreateGroupCodePageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onCreateGroupCodePage.first, pauses.onCreateGroupCodePage.second)
            .exec(
                pageOrchestrationService.postCreateGroupCodePageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterCreateGroupCodePage.first, pauses.afterCreateGroupCodePage.second)
            .exec(
                pageOrchestrationService.getGroupStartDatePageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onGroupStartDatePage.first, pauses.onGroupStartDatePage.second)
            .exec(
                pageOrchestrationService.postGroupStartDatePageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterGroupStartDatePage.first, pauses.afterGroupStartDatePage.second)
            .exec(
                pageOrchestrationService.getGroupDaysAndTimesPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onGroupDaysAndTimesPage.first, pauses.onGroupDaysAndTimesPage.second)
            .exec(
                pageOrchestrationService.postGroupDaysAndTimesPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterGroupDaysAndTimesPage.first, pauses.afterGroupDaysAndTimesPage.second)
            .exec(
                pageOrchestrationService.getGroupCohortPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onGroupCohortPage.first, pauses.onGroupCohortPage.second)
            .exec(
                pageOrchestrationService.postGroupCohortPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterGroupCohortPage.first, pauses.afterGroupCohortPage.second)
            .exec(
                pageOrchestrationService.getGroupGenderPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onGroupGenderPage.first, pauses.onGroupGenderPage.second)
            .exec(
                pageOrchestrationService.postGroupGenderPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterGroupGenderPage.first, pauses.afterGroupGenderPage.second)
            .exec(
                pageOrchestrationService.getProbationDeliveryUnitPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onProbationDeliveryUnitPage.first, pauses.onProbationDeliveryUnitPage.second)
            .exec(
                pageOrchestrationService.postProbationDeliveryUnitPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterProbationDeliveryUnitPage.first, pauses.afterProbationDeliveryUnitPage.second)
            .exec(
                pageOrchestrationService.getDeliveryLocationPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onDeliveryLocationPage.first, pauses.onDeliveryLocationPage.second)
            .exec(
                pageOrchestrationService.postDeliveryLocationPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterDeliveryLocationPage.first, pauses.afterDeliveryLocationPage.second)
            .exec(
                pageOrchestrationService.getGroupFacilitatorsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onGroupFacilitatorsPage.first, pauses.onGroupFacilitatorsPage.second)
            .exec(
                pageOrchestrationService.postGroupFacilitatorsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterGroupFacilitatorsPage.first, pauses.afterGroupFacilitatorsPage.second)
            .exec(
                pageOrchestrationService.getGroupReviewDetailsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.onGroupReviewDetailsPage.first, pauses.onGroupReviewDetailsPage.second)

        return CoreDsl.scenario(scenarioName)
            .exec(caseListChainBuilder)
    }
}