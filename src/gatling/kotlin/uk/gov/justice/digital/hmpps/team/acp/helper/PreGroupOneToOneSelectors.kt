package uk.gov.justice.digital.hmpps.team.acp.helper

import io.gatling.javaapi.core.CheckBuilder.Final
import io.gatling.javaapi.core.CoreDsl

object PreGroupOneToOneSelectors {
    const val HEADING_TEXT = "Sessions and attendance"

    fun headingCheck(): Final = CoreDsl.css("h1").find().`is`(HEADING_TEXT)
}
