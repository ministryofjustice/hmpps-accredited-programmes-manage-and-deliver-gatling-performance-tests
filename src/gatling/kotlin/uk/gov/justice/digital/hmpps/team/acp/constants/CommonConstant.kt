package uk.gov.justice.digital.hmpps.team.acp.constants

import uk.gov.justice.digital.hmpps.config.ConfigResolver

val REGION_NAME = ConfigResolver.get("region_name") ?: "Greater Manchester"
val FULL_SIMULATION_TEST_DURATION_MINUTES: Long =
    ConfigResolver.get("full_simulation_test_duration_minutes")?.toLongOrNull() ?: 5L
