package uk.gov.justice.digital.hmpps.config

data class AuthConfig(
    val authBaseUrl: String? = ConfigResolver.get("authBaseUrl"),
    val acpCookie: String? = ConfigResolver.get("hmpps-accredited-programmes-manage-and-deliver-ui.session")
)
