package uk.gov.justice.digital.hmpps.config

data class HttpRequestConfig(
    val protocol: String = ConfigResolver.require("protocol"),
    val domain: String = ConfigResolver.require("domain"),
    val acceptHeader: String = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    val acceptLanguageHeader: String = "en-US,en;q=0.5",
    val acceptEncodingHeader: String = "gzip, deflate",
    val userAgentHeader: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0",
)
