package routes

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import data.books
import model.Book

fun Route.bookRoutes() {
    route("/books") {

        get {
            call.respond(books)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val book = books.find { it.id == id }
            if (book != null) {
                call.respond(book)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            }
        }

        post {
            val book = call.receive<Book>()
            books.add(book)
            call.respond(HttpStatusCode.Created, book)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val index = books.indexOfFirst { it.id == id }
            if (index != -1) {
                val updated = call.receive<Book>()
                books[index] = updated
                call.respond(HttpStatusCode.OK, updated)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val removed = books.removeIf { it.id == id }
            if (removed) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
