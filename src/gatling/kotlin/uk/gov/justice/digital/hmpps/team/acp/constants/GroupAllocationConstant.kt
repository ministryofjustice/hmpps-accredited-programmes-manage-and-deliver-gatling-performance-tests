package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver
import uk.gov.justice.digital.hmpps.team.acp.model.GroupAllocationPauseConfig

val NO_OF_GROUP_ALLOCATION_USERS: Int =
    ConfigResolver.get("group_allocation_concurrent_users")?.toIntOrNull() ?: 1
val GROUP_ALLOCATION_TEST_DURATION_MINUTES: Long =
    ConfigResolver.get("group_allocation_test_duration_minutes")?.toLongOrNull() ?: 5L

val groupAllocationPauseConfig =
    GroupAllocationPauseConfig(
        beforeStart = 0L to 2L,
        onWaitlistPage = 2L to 5L,
        afterWaitlistPage = 2L to 5L,
        onAddToGroupConfirmPage = 2L to 5L,
        afterAddToGroupConfirmPage = 2L to 5L,
        onScheduledStatusDetailsPage = 2L to 5L,
        afterScheduledStatusDetailsPage = 2L to 5L,
        onAllocatedListPage = 2L to 5L,
        afterAllocatedListPage = 2L to 5L,
    )

const val ADD_TO_GROUP_CONFIRM_YES: String = "yes"
const val ADD_TO_GROUP_ADDITIONAL_DETAILS: String = "Added via performance test"
