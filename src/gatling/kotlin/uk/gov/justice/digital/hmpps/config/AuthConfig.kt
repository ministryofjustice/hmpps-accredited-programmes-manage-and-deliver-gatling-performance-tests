package uk.gov.justice.digital.hmpps.config

data class AuthConfig(
    val authBaseUrl: String =
        ConfigResolver.get("authBaseUrl")
            ?: "https://sign-in-dev.hmpps.service.justice.gov.uk",
    val username: String? = ConfigResolver.get("auth_username"),
    val password: String? = ConfigResolver.get("auth_password"),
    val acpCookie: String? = ConfigResolver.get("hmpps-accredited-programmes-manage-and-deliver-ui.session"),
)
