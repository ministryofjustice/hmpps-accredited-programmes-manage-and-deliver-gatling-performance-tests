package uk.gov.justice.digital.hmpps.config

data class DbConfig(
    val dbPort: Int = System.getProperty("db_port")?.toIntOrNull()
        ?: error("System property 'db_port' must be set to a valid integer. Pass -Ddb_port=<port>"),

    val dbName: String = System.getProperty("db_name")
        ?: error("System property 'db_name' must be set. Pass -Ddb_name=<value>"),

    val dbUsername: String = System.getProperty("db_username")
        ?: error("System property 'db_username' must be set. Pass -Ddb_username=<value>"),

    val dbPassword: String = System.getProperty("db_password")
        ?: error("System property 'db_password' must be set. Pass -Ddb_password=<value>")
)