package uk.gov.justice.digital.hmpps.config

data class DbConfig(
    val dbPort: Int = System.getProperty("db_port").toInt(),
    val dbName: String = System.getProperty("db_name"),
    val dbUsername: String = System.getProperty("db_username"),
    val dbPassword: String = System.getProperty("db_password")
)
