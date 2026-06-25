import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "dev.nexus"
version = "0.9.0"

application {
    mainClass.set("dev.nexus.api.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${project.findProperty("development") ?: "false"}")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.logback.classic)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.hikari)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)

    // Auth & security
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.bcrypt)

    // Cache
    implementation(libs.lettuce)

    // Messaging
    implementation(libs.kafka.clients)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

// The fat jar merges many deps' META-INF/services/ files. Without merging, one
// dependency's ServiceLoader file clobbers another's — which silently drops
// Flyway's SQL-migration resolver plugins (flyway-core) when only the
// flyway-database-postgresql entries survive. mergeServiceFiles() concatenates
// them so every plugin is discovered.
tasks.withType<ShadowJar> {
    mergeServiceFiles()
}
