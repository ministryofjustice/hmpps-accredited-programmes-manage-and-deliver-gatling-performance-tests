package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.helper.GroupCodeGenerator
import uk.gov.justice.digital.hmpps.team.acp.jdbc.CreateGroupRepository
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupPauseConfig
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupSimulationSession
import java.time.Duration

class CreateGroupScenarioService(
    private val httpRequestHelper: HttpRequestHelper = HttpRequestHelper(),
    private val signInService: SignInService = SignInService(),
    private val createGroupRepository: CreateGroupRepository = CreateGroupRepository(),
    private val pageOrchestrationService: CreateGroupPageOrchestrationService = CreateGroupPageOrchestrationService(),
) {
    fun buildScenario(
        scenarioName: String,
        pauses: CreateGroupPauseConfig,
        journeyDuration: Duration,
    ): ScenarioBuilder {
        // Authenticate via HMPPS-Auth
        val authenticate =
            httpRequestHelper.acpAuthCookie
                ?.let { CoreDsl.exec(HttpDsl.addCookie(it)) }
                ?: signInService.signIn()

        val singleCreateGroupJourney =
            CoreDsl
                .pause(pauses.beforeStart.first, pauses.beforeStart.second)
                .exec(pageOrchestrationService.getCreateGroupPageAndDoChecks())
                .pause(pauses.onCreateGroupPage.first, pauses.onCreateGroupPage.second)
                .exec(pageOrchestrationService.postCreateGroupPageAndDoChecks())
                .pause(pauses.afterCreateGroupPage.first, pauses.afterCreateGroupPage.second)
                .exec(pageOrchestrationService.getCreateGroupCodePageAndDoChecks())
                .pause(pauses.onCreateGroupCodePage.first, pauses.onCreateGroupCodePage.second)
                .exec { session ->
                    session.set(CreateGroupSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }.exec(pageOrchestrationService.postCreateGroupCodePageAndDoChecks())
                .pause(pauses.afterCreateGroupCodePage.first, pauses.afterCreateGroupCodePage.second)
                .exec(pageOrchestrationService.getGroupStartDatePageAndDoChecks())
                .pause(pauses.onGroupStartDatePage.first, pauses.onGroupStartDatePage.second)
                .exec(pageOrchestrationService.postGroupStartDatePageAndDoChecks())
                .pause(pauses.afterGroupStartDatePage.first, pauses.afterGroupStartDatePage.second)
                .exec(pageOrchestrationService.getGroupDaysAndTimesPageAndDoChecks())
                .pause(pauses.onGroupDaysAndTimesPage.first, pauses.onGroupDaysAndTimesPage.second)
                .exec(pageOrchestrationService.postGroupDaysAndTimesPageAndDoChecks())
                .pause(pauses.afterGroupDaysAndTimesPage.first, pauses.afterGroupDaysAndTimesPage.second)
                .exec(pageOrchestrationService.getGroupCohortPageAndDoChecks())
                .pause(pauses.onGroupCohortPage.first, pauses.onGroupCohortPage.second)
                .exec(pageOrchestrationService.postGroupCohortPageAndDoChecks())
                .pause(pauses.afterGroupCohortPage.first, pauses.afterGroupCohortPage.second)
                .exec(pageOrchestrationService.getGroupGenderPageAndDoChecks())
                .pause(pauses.onGroupGenderPage.first, pauses.onGroupGenderPage.second)
                .exec(pageOrchestrationService.postGroupGenderPageAndDoChecks())
                .pause(pauses.afterGroupGenderPage.first, pauses.afterGroupGenderPage.second)
                .exec(pageOrchestrationService.getProbationDeliveryUnitPageAndDoChecks())
                .pause(pauses.onProbationDeliveryUnitPage.first, pauses.onProbationDeliveryUnitPage.second)
                .exec(pageOrchestrationService.postProbationDeliveryUnitPageAndDoChecks())
                .pause(pauses.afterProbationDeliveryUnitPage.first, pauses.afterProbationDeliveryUnitPage.second)
                .exec(pageOrchestrationService.getDeliveryLocationPageAndDoChecks())
                .pause(pauses.onDeliveryLocationPage.first, pauses.onDeliveryLocationPage.second)
                .exec(pageOrchestrationService.postDeliveryLocationPageAndDoChecks())
                .pause(pauses.afterDeliveryLocationPage.first, pauses.afterDeliveryLocationPage.second)
                .exec(pageOrchestrationService.getGroupFacilitatorsPageAndDoChecks())
                .pause(pauses.onGroupFacilitatorsPage.first, pauses.onGroupFacilitatorsPage.second)
                .exec(pageOrchestrationService.postGroupFacilitatorsPageAndDoChecks())
                .pause(pauses.afterGroupFacilitatorsPage.first, pauses.afterGroupFacilitatorsPage.second)
                .exec(pageOrchestrationService.getGroupReviewDetailsPageAndDoChecks())
                .pause(pauses.onGroupReviewDetailsPage.first, pauses.onGroupReviewDetailsPage.second)
                .exec(pageOrchestrationService.postGroupReviewDetailsPageAndDoChecks())
                .pause(pauses.afterGroupReviewDetailsPage.first, pauses.afterGroupReviewDetailsPage.second)
                .exec { session ->
                    val groupCode =
                        requireNotNull(session.getString(CreateGroupSimulationSession.GROUP_CODE.sessionKey)) {
                            "GROUP_CODE is missing from Gatling session"
                        }
                    val groupId =
                        requireNotNull(createGroupRepository.findGroupIdByCode(groupCode)) {
                            "Group ID not found for group code: $groupCode"
                        }
                    session.set(CreateGroupSimulationSession.GROUP_ID.sessionKey, groupId.toString())
                }.exec(pageOrchestrationService.getGroupCreatedPageAndDoChecks())
                .pause(pauses.onGroupCreatedPage.first, pauses.onGroupCreatedPage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .during(journeyDuration)
            .on(
                CoreDsl.exitBlockOnFail().on(singleCreateGroupJourney),
            )
    }
}
