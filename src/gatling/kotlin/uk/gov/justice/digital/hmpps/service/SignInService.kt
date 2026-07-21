package uk.gov.justice.digital.hmpps.service

import io.gatling.javaapi.core.ChainBuilder
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.config.AuthConfig
import uk.gov.justice.digital.hmpps.config.HttpRequestConfig

// Signs the virtual user in through HMPPS Auth.
class SignInService(
    private val authConfig: AuthConfig = AuthConfig(),
    private val httpRequestConfig: HttpRequestConfig = HttpRequestConfig(),
) {
    fun signIn(): ChainBuilder {
        val username =
            checkNotNull(authConfig.username) {
                "Configuration value 'auth_username' is not set — required to sign in through HMPPS Auth. " +
                    "Alternatively set hmpps-accredited-programmes-manage-and-deliver-ui.session to reuse an existing session."
            }
        val password =
            checkNotNull(authConfig.password) {
                "Configuration value 'auth_password' is not set — required to sign in through HMPPS Auth."
            }

        return CoreDsl
            .exec(
                // Redirects through HMPPS Auth and lands on its sign-in form
                HttpDsl
                    .http("GET - Sign in page")
                    .get("/sign-in")
                    .check(HttpDsl.status().shouldBe(200))
                    .check(
                        CoreDsl
                            .css("input[name='_csrf']", "value")
                            .find()
                            .notNull()
                            .saveAs(SIGN_IN_CSRF_KEY),
                    ).check(
                        CoreDsl
                            .css("input[name='redirect_uri']", "value")
                            .find()
                            .saveAs(SIGN_IN_REDIRECT_URI_KEY),
                    ),
            ).exitHereIfFailed()
            .exec(
                HttpDsl
                    .http("POST - Sign in credentials")
                    .post("${authConfig.authBaseUrl}/auth/sign-in")
                    .formParam("_csrf", "#{$SIGN_IN_CSRF_KEY}")
                    .formParam("redirect_uri", "#{$SIGN_IN_REDIRECT_URI_KEY}")
                    .formParam("username", username)
                    .formParam("password", password)
                    .check(HttpDsl.currentLocation().saveAs(SIGN_IN_LANDING_URL_KEY))
                    .check(HttpDsl.status().shouldBe(200))
                    .check(
                        HttpDsl
                            .currentLocationRegex("https?://${Regex.escape(httpRequestConfig.domain)}(/.*)?")
                            .find()
                            .exists(),
                    ),
            ).exec { session ->
                if (session.isFailed) {
                    System.err.println(
                        "Sign-in failed — expected to land on ${httpRequestConfig.domain} " +
                            "but landed on: ${session.getString(SIGN_IN_LANDING_URL_KEY)}",
                    )
                }
                session
            }.exitHereIfFailed()
    }

    companion object {
        private const val SIGN_IN_CSRF_KEY = "signInCsrfToken"
        private const val SIGN_IN_REDIRECT_URI_KEY = "signInRedirectUri"
        private const val SIGN_IN_LANDING_URL_KEY = "signInLandingUrl"
    }
}
