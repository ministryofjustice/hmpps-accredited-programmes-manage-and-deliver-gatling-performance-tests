package uk.gov.justice.digital.hmpps.team.acp.helper

import io.gatling.javaapi.core.CheckBuilder.Final
import io.gatling.javaapi.core.CoreDsl

class AcpSelectorHelper {
    fun getCsrfHiddenFieldValue(sessionKey: String): Final =
        CoreDsl.css("input", "value")
            .findAll()
            .transformWithSession { valuesFound, _ ->
                valuesFound[0]
            }.notNull().saveAs(sessionKey)

}