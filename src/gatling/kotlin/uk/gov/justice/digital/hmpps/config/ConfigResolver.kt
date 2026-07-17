package uk.gov.justice.digital.hmpps.config

import java.io.File
import java.util.Properties

/**
 * Resolves configuration values from, in order of precedence:
 * 1. JVM system properties (-Dkey=value)
 * 2. Environment variables (key transformed to UPPER_SNAKE_CASE, e.g. db_port -> DB_PORT)
 * 3. A local properties file (local.properties in the project root by default, gitignored)
 *
 * The properties file location can be overridden with -Dconfig.file=<path> or the
 * CONFIG_FILE environment variable.
 */
object ConfigResolver {
    private const val CONFIG_FILE_KEY = "config.file"
    private const val DEFAULT_CONFIG_FILE = "local.properties"

    private val fileProperties: Properties by lazy {
        Properties().apply {
            val path = System.getProperty(CONFIG_FILE_KEY)
                ?: System.getenv(CONFIG_FILE_KEY.toEnvVarName())
                ?: DEFAULT_CONFIG_FILE
            val file = File(path)
            if (file.exists()) {
                file.reader().use { load(it) }
            }
        }
    }

    fun get(key: String): String? =
        System.getProperty(key)
            ?: System.getenv(key.toEnvVarName())
            ?: fileProperties.getProperty(key)?.takeIf { it.isNotBlank() }

    fun require(key: String): String = get(key) ?: error(
        "Configuration value '$key' is not set. Provide it via -D$key=<value>, " +
            "the ${key.toEnvVarName()} environment variable, or an entry in $DEFAULT_CONFIG_FILE " +
            "(copy local.properties.example to get started)."
    )

    fun requireInt(key: String): Int = require(key).toIntOrNull()
        ?: error("Configuration value '$key' must be a valid integer.")

    private fun String.toEnvVarName() = uppercase().replace(Regex("[^A-Z0-9]"), "_")
}
