package com.example

import io.ktor.server.routing.*
import io.ktor.server.application.*
import routes.bookRoutes
import routes.lendingRoutes

fun Application.configureRouting() {
    routing {
        bookRoutes()
        lendingRoutes()
    }
}
