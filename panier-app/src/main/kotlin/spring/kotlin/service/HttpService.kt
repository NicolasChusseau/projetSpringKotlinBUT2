package spring.kotlin.service

import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class HttpService() {

    private val baseUrl = "http://localhost:8081/api/"
    private val client: HttpClient = HttpClient.newHttpClient()

    // Exécute une requête HTTP GET
    fun get(endpoint: String): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl$endpoint"))
                .header("Content-Type", "application/json")
                .GET()
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == 200) {
            return response.body()
        } else {
            throw RuntimeException("GET request failed with HTTP error code: ${response.statusCode()}")
        }
    }


}







