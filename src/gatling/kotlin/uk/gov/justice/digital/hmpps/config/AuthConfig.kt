package uk.gov.justice.digital.hmpps.config

data class AuthConfig(
    val authBaseUrl: String? = System.getProperty("authBaseUrl"),
    val acpCookie: String? = System.getProperty("hmpps-accredited-programmes-manage-and-deliver-ui.session")
)
