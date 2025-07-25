package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}


fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}
