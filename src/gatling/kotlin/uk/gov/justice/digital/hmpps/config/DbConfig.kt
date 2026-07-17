package uk.gov.justice.digital.hmpps.config

data class DbConfig(
    val dbPort: Int = ConfigResolver.requireInt("db_port"),
    val dbName: String = ConfigResolver.require("db_name"),
    val dbUsername: String = ConfigResolver.require("db_username"),
    val dbPassword: String = ConfigResolver.require("db_password"),
)
