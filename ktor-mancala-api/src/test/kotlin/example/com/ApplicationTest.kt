package example.com

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testGetMancala() = testApplication {
        client.get("/mancala").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testPlayMove() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/play") {
            contentType(ContentType.Application.Json)
            setBody(Pit(3))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}

@Serializable
data class Pit(
    var index: Int = 5
)

