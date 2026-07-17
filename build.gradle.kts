plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")

    // The following line allows to load io.gatling.gradle plugin and directly apply it
    id("io.gatling.gradle") version "3.15.1.1"
}

gatling {
    enterprise.closureOf<Any> {
        // Enterprise Cloud (https://cloud.gatling.io/) configuration reference: https://docs.gatling.io/reference/integrations/build-tools/gradle-plugin/
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    mavenCentral()
}

dependencies {
    gatling("org.postgresql:postgresql:42.6.0")

    gatling(platform("org.http4k:http4k-bom:5.12.0.0"))
    gatling("org.http4k:http4k-core")
    gatling("org.http4k:http4k-server-undertow")
    gatling("org.http4k:http4k-client-apache")
}