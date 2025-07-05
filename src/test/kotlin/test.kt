import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import model.Book
import kotlinx.serialization.json.Json
import com.example.module
import model.LendingRecord
import kotlinx.serialization.encodeToString

class BookRoutesTest {

    @Test
    fun testGetAllBooks() = testApplication {
        application {
            module()
        }

        val response = client.get("/books")
        assertEquals(HttpStatusCode.OK, response.status)

        val json = response.bodyAsText()
        val books = Json.decodeFromString<List<Book>>(json)
        assertTrue(books.isNotEmpty(), "Books should not be empty")
    }

    @Test
    fun testGetBookById() = testApplication {
        application {
            module()
        }

        val response = client.get("/books/1")
        assertEquals(HttpStatusCode.OK, response.status)

        val book = Json.decodeFromString<Book>(response.bodyAsText())
        assertEquals(1, book.id)
        assertEquals("1984", book.title)
    }

    @Test
    fun testAddBook() = testApplication {
        application {
            module()
        }

        val newBook = Book(99, "New Book", "New Author", true)
        val response = client.post("/books") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(Book.serializer(), newBook))
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val added = Json.decodeFromString<Book>(response.bodyAsText())
        assertEquals("New Book", added.title)
    }

    @Test
    fun testDeleteBook() = testApplication {
        application {
            module()
        }

        val response = client.delete("/books/1")
        assertTrue(response.status == HttpStatusCode.NoContent || response.status == HttpStatusCode.NotFound)
    }

}

class LendingRoutesTest {

    @Test
    fun testLendBook() = testApplication {
        application { module() }

        val lending = LendingRecord(
            id = 100,
            bookId = 1,
            borrowerName = "Alice",
            checkoutDate = "2025-07-05"
        )

        val response = client.post("/lendings") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(lending))
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val created = Json.decodeFromString<LendingRecord>(response.bodyAsText())
        assertEquals(lending.id, created.id)
        assertEquals(null, created.returnDate)
    }

    @Test
    fun testReturnBook() = testApplication {
        application { module() }

        val lending = LendingRecord(
            id = 200,
            bookId = 2,
            borrowerName = "Bob",
            checkoutDate = "2025-07-06"
        )

        val postResponse = client.post("/lendings") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(lending))
        }
        assertEquals(HttpStatusCode.Created, postResponse.status)


        val putResponse = client.put("/lendings/200/return")

        assertEquals(HttpStatusCode.OK, putResponse.status)

        val returned = Json.decodeFromString<LendingRecord>(putResponse.bodyAsText())
        assertNotNull(returned.returnDate)
    }
}
