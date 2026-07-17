package uk.gov.justice.digital.hmpps.team.acp.helper

import io.gatling.javaapi.core.CheckBuilder.Final
import io.gatling.javaapi.core.CoreDsl

class AcpSelectorHelper {
    fun getCsrfHiddenFieldValue(sessionKey: String): Final =
        CoreDsl
            .css("input[name='_csrf']", "value")
            .find()
            .notNull()
            .saveAs(sessionKey)
}
