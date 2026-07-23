package uk.gov.justice.digital.hmpps.team.acp.service

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.helper.HttpRequestHelper
import uk.gov.justice.digital.hmpps.service.SignInService
import uk.gov.justice.digital.hmpps.team.acp.helper.GroupCodeGenerator
import uk.gov.justice.digital.hmpps.team.acp.jdbc.CaseListFeeder
import uk.gov.justice.digital.hmpps.team.acp.jdbc.GroupDetailsFeeder
import uk.gov.justice.digital.hmpps.team.acp.model.CreateGroupSimulationSession
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
                .exec(pageOrchestrationService.getEditGroupCodePageAndDoChecks())
                .pause(pauses.onEditGroupCodePage.first, pauses.onEditGroupCodePage.second)
                .exec { session ->
                    session.set(GroupDetailsSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
                }
                .pause(pauses.onEditGroupCodePage.first, pauses.onEditGroupCodePage.second)
                .exec(pageOrchestrationService.postEditGroupCodePageAndDoChecks())

//                .exec(pageOrchestrationService.getCreateGroupCodePageAndDoChecks())
//                .pause(pauses.onCreateGroupCodePage.first, pauses.onCreateGroupCodePage.second)
//                .exec { session ->
//                    session.set(CreateGroupSimulationSession.GROUP_CODE.sessionKey, GroupCodeGenerator.next())
//                }.exec(pageOrchestrationService.postCreateGroupCodePageAndDoChecks())
//                .pause(pauses.afterCreateGroupCodePage.first, pauses.afterCreateGroupCodePage.second)
//                .exec(pageOrchestrationService.getGroupStartDatePageAndDoChecks())
//                .pause(pauses.onGroupStartDatePage.first, pauses.onGroupStartDatePage.second)
//                .exec(pageOrchestrationService.postGroupStartDatePageAndDoChecks())
//                .pause(pauses.afterGroupStartDatePage.first, pauses.afterGroupStartDatePage.second)
//                .exec(pageOrchestrationService.getGroupDaysAndTimesPageAndDoChecks())
//                .pause(pauses.onGroupDaysAndTimesPage.first, pauses.onGroupDaysAndTimesPage.second)
//                .exec(pageOrchestrationService.postGroupDaysAndTimesPageAndDoChecks())
//                .pause(pauses.afterGroupDaysAndTimesPage.first, pauses.afterGroupDaysAndTimesPage.second)
//                .exec(pageOrchestrationService.getGroupCohortPageAndDoChecks())
//                .pause(pauses.onGroupCohortPage.first, pauses.onGroupCohortPage.second)
//                .exec(pageOrchestrationService.postGroupCohortPageAndDoChecks())
//                .pause(pauses.afterGroupCohortPage.first, pauses.afterGroupCohortPage.second)
//                .exec(pageOrchestrationService.getGroupGenderPageAndDoChecks())
//                .pause(pauses.onGroupGenderPage.first, pauses.onGroupGenderPage.second)
//                .exec(pageOrchestrationService.postGroupGenderPageAndDoChecks())
//                .pause(pauses.afterGroupGenderPage.first, pauses.afterGroupGenderPage.second)
//                .exec(pageOrchestrationService.getProbationDeliveryUnitPageAndDoChecks())
//                .pause(pauses.onProbationDeliveryUnitPage.first, pauses.onProbationDeliveryUnitPage.second)
//                .exec(pageOrchestrationService.postProbationDeliveryUnitPageAndDoChecks())
//                .pause(pauses.afterProbationDeliveryUnitPage.first, pauses.afterProbationDeliveryUnitPage.second)
//                .exec(pageOrchestrationService.getDeliveryLocationPageAndDoChecks())
//                .pause(pauses.onDeliveryLocationPage.first, pauses.onDeliveryLocationPage.second)
//                .exec(pageOrchestrationService.postDeliveryLocationPageAndDoChecks())
//                .pause(pauses.afterDeliveryLocationPage.first, pauses.afterDeliveryLocationPage.second)
//                .exec(pageOrchestrationService.getGroupFacilitatorsPageAndDoChecks())
//                .pause(pauses.onGroupFacilitatorsPage.first, pauses.onGroupFacilitatorsPage.second)
//                .exec(pageOrchestrationService.postGroupFacilitatorsPageAndDoChecks())
//                .pause(pauses.afterGroupFacilitatorsPage.first, pauses.afterGroupFacilitatorsPage.second)
//                .exec(pageOrchestrationService.getGroupReviewDetailsPageAndDoChecks())
//                .pause(pauses.onGroupReviewDetailsPage.first, pauses.onGroupReviewDetailsPage.second)
//                .exec(pageOrchestrationService.postGroupReviewDetailsPageAndDoChecks())
                .pause(pauses.afterEditGroupCodePage.first, pauses.afterEditGroupCodePage.second)

        return CoreDsl
            .scenario(scenarioName)
            .exec(authenticate)
            .during(journeyDuration)
            .on(
                CoreDsl.exitBlockOnFail().on(singleGroupDetailsJourney),
            )
    }
}
