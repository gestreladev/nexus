package dev.nexus.api

import dev.nexus.api.pipeline.nexusModule
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() = nexusModule {
    logging()
    serialization()
    statusPages()
    database()
    authentication()
    routing()
}
