package routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.routing.*
import model.LendingRecord
import data.books
import data.lendings

fun Route.lendingRoutes() {

    route("/lendings") {

        get {
            call.respond(lendings)
        }

        post {
            val newLending = call.receive<LendingRecord>()
            val book = books.find { it.id == newLending.bookId }

            if (book == null) {
                call.respond(HttpStatusCode.BadRequest, "Book not found")
                return@post
            }

            if (!book.isAvailable) {
                call.respond(HttpStatusCode.Conflict, "Book is not available for lending")
                return@post
            }

            books[books.indexOf(book)] = book.copy(isAvailable = false)
            lendings.add(newLending)
            call.respond(HttpStatusCode.Created, newLending)
        }

        put("{id}/return") {
            val id = call.parameters["id"]?.toIntOrNull()
            val lending = lendings.find { it.id == id }

            if (lending == null) {
                call.respond(HttpStatusCode.NotFound, "Lending record not found")
                return@put
            }

            if (lending.returnDate != null) {
                call.respond(HttpStatusCode.Conflict, "Book already returned")
                return@put
            }

            val updated = lending.copy(returnDate = java.time.LocalDate.now().toString())
            lendings[lendings.indexOf(lending)] = updated

            val book = books.find { it.id == lending.bookId }
            if (book != null) {
                books[books.indexOf(book)] = book.copy(isAvailable = true)
            }

            call.respond(HttpStatusCode.OK, updated)
        }
    }
}
