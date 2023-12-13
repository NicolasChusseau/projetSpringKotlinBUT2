package spring.kotlin.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate

@Service
class HttpService {

    private val baseUrlArticle = "http://localhost:8081/api/"
    private val baseUrlUser = "http://localhost:8080/api/"
    private val client: HttpClient = HttpClient.newHttpClient()

    // Exécute une requête HTTP GET
    fun get(endpoint: String): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrlArticle$endpoint"))
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

    fun getUser(endpoint: String): String {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrlUser$endpoint"))
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


    // Put qui décrémente la quantité d'un article
    fun decreaseQuantity(id: Int, quantity: Int) {
        //TODO: à compléter une fois que l'api article sera disponible
        val request = HttpRequest.newBuilder()
                .uri(URI.create("${baseUrlArticle}articles/decreaseQuantity/$id/$quantity"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"id\":$id,\"qteStock\":$quantity}"))
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw RuntimeException("PUT request failed with HTTP error code: ${response.statusCode()}")
        }
    }

    // Put qui change la date de dernière commande du user
    fun updateLastOrderDate(userEmail: String) {
        //On get le user
        val res = getUser("users/${userEmail}")
        //On transforme le String en UserDTO
        val user = ObjectMapper().readValue(res, User::class.java)
        //On change la date de dernière commande
        user.dateDerniereCommande = LocalDate.now().toString()

        //On transforme le UserDTO en String sans ObjectMapper car il ne peut pas sérialiser les LocalDate
        val userString = ObjectMapper().writeValueAsString(user)

        //On fait le put vers l'api des users :)
        val request = HttpRequest.newBuilder()
                .uri(URI.create("${baseUrlUser}users/$userEmail"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(userString))
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw RuntimeException("PUT request failed with HTTP error code: ${response.statusCode()}")
        }
    }

}

class User(
        val email: String,
        val nom: String,
        val adresseDeLivraison: String,
        val estAbonnee: Boolean,
        var dateDerniereCommande: String?,
        val age: Int
) {
    constructor() : this("", "", "", false, null, 0)
}





