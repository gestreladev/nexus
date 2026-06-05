package dev.nexus.api

import dev.nexus.api.plugins.configureRouting
import dev.nexus.api.plugins.configureSerialization
import dev.nexus.api.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/v1") }
    }
    configureSerialization()
    configureStatusPages()
    configureRouting()
}
