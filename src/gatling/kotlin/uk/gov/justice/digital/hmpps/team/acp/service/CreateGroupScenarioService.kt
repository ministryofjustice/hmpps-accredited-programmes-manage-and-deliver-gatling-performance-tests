package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.team.acp.jdbc.CreateGroupRepository
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupSimulationSession
import uk.gov.justice.digital.hmpps.team.acp.helper.GroupCodeGenerator

class CreateGroupScenarioService(
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val createGroupRepository: CreateGroupRepository = CreateGroupRepository(),
    private val pageOrchestrationService: CreateGroupPageOrchestrationService = CreateGroupPageOrchestrationService()

) {
    fun buildScenario(
        scenarioName: String,
        pauses: CreateGroupPauseConfig

    ): ScenarioBuilder {
        val cookie = checkNotNull(httpRequestHelper.acpAuthCookie) {
            "acpAuthCookie is null — did you pass -Dhmpps-accredited-programmes-manage-and-deliver-ui.session=<session-cookie>?"
        }

        val createGroupChainBuilder = CoreDsl.exec(HttpDsl.addCookie(cookie))
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
            .exec { session ->
                val groupCode = GroupCodeGenerator.next()
                session.set(
                    CreateGroupSimulationSession.GROUP_CODE.sessionKey,
                    groupCode
                )
            }
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
            .exec(
                pageOrchestrationService.postGroupReviewDetailsPageAndDoChecks()
            )
            .exitHereIfFailed()
            .pause(pauses.afterGroupReviewDetailsPage.first, pauses.afterGroupReviewDetailsPage.second)
            .exec { session ->

                val groupCode = requireNotNull(
                    session.getString(CreateGroupSimulationSession.GROUP_CODE.sessionKey)
                ) {
                    "GROUP_CODE is missing from Gatling session"
                }
                println("******** GROUP CODE = $groupCode")

                val groupId = requireNotNull(
                    createGroupRepository.findGroupIdByCode(groupCode)
                ) {
                    "Group ID not found for group code: $groupCode"
                }
                println("******** GROUP ID = $groupId")

                session.set(
                    CreateGroupSimulationSession.GROUP_ID.sessionKey,
                    groupId.toString()
                )
            }
            .exec(
               pageOrchestrationService.getGroupCreatedPageAndDoChecks()
            )
           .exitHereIfFailed()
            .pause(pauses.onGroupCreatedPage.first, pauses.onGroupCreatedPage.second)

        return CoreDsl.scenario(scenarioName)
            .exec(createGroupChainBuilder)
    }
}