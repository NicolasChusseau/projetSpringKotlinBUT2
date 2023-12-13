package spring.kotlin.service

import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class HttpService() {

    private val baseUrl = "http://localhost:8082/api/"
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

    // Post
    fun post(endpoint: String, body: String): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl$endpoint"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        // Le code de réponse 201 est pour les posts
        if (response.statusCode() == 201) {
            return response.body()
        } else {
            throw RuntimeException("POST request failed with HTTP error code: ${response.statusCode()}")
        }
    }


}







