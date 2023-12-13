package spring.kotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.kotlin.controller.dto.PanierDTO
import spring.kotlin.controller.dto.asPanierDTO
import spring.kotlin.domain.ArticlePanier
import spring.kotlin.domain.Panier
import spring.kotlin.errors.NotEnoughStockError
import spring.kotlin.errors.PanierNotFoundError
import spring.kotlin.repository.PanierRepository
import spring.kotlin.service.HttpService
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@RestController
@Validated
class PanierController(val panierRepository: PanierRepository, val httpService: HttpService) {

    @Operation(summary = "Create panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Panier created",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class)
                )]),
        ApiResponse(responseCode = "409", description = "Panier already exist",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/paniers")
    fun create(@RequestBody @Valid userEmail: String): ResponseEntity<PanierDTO> =
            panierRepository.create(Panier(userEmail, mutableListOf())).fold(
                    { success -> ResponseEntity.status(HttpStatus.CREATED).body(success.asPanierDTO()) },
                    { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })


    @Operation(summary = "List paniers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List paniers",
                content = [Content(mediaType = "application/json",
                        array = ArraySchema(
                                schema = Schema(implementation = PanierDTO::class))
                )])])
    @GetMapping("/api/paniers")
    fun list() =
            panierRepository.list()
                    .map { it.asPanierDTO() }
                    .let {
                        ResponseEntity.ok(it)
                    }

    @Operation(summary = "Get panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @GetMapping("/api/paniers/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<PanierDTO> {
        val panier = panierRepository.get(id)
        return if (panier != null) {
            ResponseEntity.ok(panier.asPanierDTO())
        } else {
            throw PanierNotFoundError(id)
        }
    }

    @Operation(summary = "Add article or quantity panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @PutMapping("/api/paniers/addQuantity/{userEmail}/{articleId}/{quantite}")
    fun addQuantityArticlePanier(
            @PathVariable userEmail: String,
            @PathVariable articleId: Int,
            @PathVariable quantite: Int
    ): ResponseEntity<Any> {
        if (panierRepository.get(userEmail) == null) {
            return ResponseEntity.badRequest().body("Email invalide")
        } else {
            //On vérifie si l'article existe en http
            val article: String
            try {
                article = httpService.get("articles/$articleId")
            } catch (e: Exception) {
                return ResponseEntity.badRequest().body("L'article n'existe pas")
            }
            //On transforme le String en ArticleDTO
            val articleDTO = ObjectMapper().readValue(article, Article::class.java)
            val quantiteDispo = articleDTO.qteStock

            if (quantite < 0) {
                return ResponseEntity.badRequest().body("La quantité demandée est négative")
            }

            val existingArticlePanier = panierRepository.get(userEmail)?.articlesPanier?.find { it.articleId == articleId }
            if (existingArticlePanier == null) {
                // L'article avec articleId n'existe pas dans le panier
                //On va vérifier ici que la quantité demandée est inférieure ou égale à la quantité disponible
                if (quantite > quantiteDispo) {
                    return ResponseEntity.badRequest().body("La quantité demandée est supérieure à la quantité disponible")
                }
                val articlePanier = panierRepository.get(userEmail)!!.articlesPanier
                articlePanier.add(ArticlePanier(articleId, quantite))
                return panierRepository.update(Panier(userEmail, articlePanier)).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            } else {
                // L'article avec articleId existe déjà dans le panier
                //On va vérifier ici que la quantité demandée + la quantité de l'article dans le panier est inférieure ou égale à la quantité disponible
                if (quantite + existingArticlePanier.quantite > quantiteDispo) {
                    return ResponseEntity.badRequest().body("La quantité demandée est supérieure à la quantité disponible")
                }
                val articlePanier = panierRepository.get(userEmail)!!.articlesPanier
                val article = articlePanier.find { it.articleId == articleId }
                article!!.quantite = existingArticlePanier.quantite + quantite
                return panierRepository.update(Panier(userEmail, articlePanier)).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }
        }
    }

    @Operation(summary = "Remove quantity panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @PutMapping("/api/paniers/removeQuantity/{userEmail}/{articleId}/{quantite}")
    fun removeQuantityArticlePanier(@PathVariable userEmail: String, @PathVariable articleId: Int, @PathVariable quantite: Int): ResponseEntity<Any> {
        if (panierRepository.get(userEmail) == null) {
            return ResponseEntity.badRequest().body("Le panier n'existe pas")
        } else {
            //On vérifie si l'article existe en http
            try {
                httpService.get("articles/$articleId")
            } catch (e: Exception) {
                return ResponseEntity.badRequest().body("L'article n'existe pas")
            }

            if (quantite < 0) {
                return ResponseEntity.badRequest().body("La quantité demandée est négative")
            }
            val existingArticle = panierRepository.get(userEmail)?.articlesPanier?.find { it.articleId == articleId }
            if (existingArticle == null) {
                // L'article avec articleId n'existe pas dans le panier
                return ResponseEntity.badRequest().body("L'article n'existe pas dans le panier")
            } else {
                // Si la quantité à retirer est supérieure à la quantité de l'article dans le panier
                val articlePanier = panierRepository.get(userEmail)!!.articlesPanier
                val article = articlePanier.find { it.articleId == articleId }
                if (quantite >= existingArticle.quantite) {
                    //On retire l'article du panier
                    articlePanier.remove(article)
                } else {
                    // On retire la quantité de l'article dans le panier
                    article!!.quantite = existingArticle.quantite - quantite
                }
                return panierRepository.update(Panier(userEmail, articlePanier)).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }
        }
    }

    @Operation(summary = "Remove panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @PutMapping("/api/paniers/removeQuantity/{userEmail}/{articleId}")
    fun removeArticlePanier(@PathVariable userEmail: String, @PathVariable articleId: Int): ResponseEntity<Any> {
        if (panierRepository.get(userEmail) == null) {
            return ResponseEntity.badRequest().body("Le panier n'existe pas")
        } else {
            //On vérifie si l'article existe en http
            try {
                httpService.get("articles/$articleId")
            } catch (e: Exception) {
                return ResponseEntity.badRequest().body("L'article n'existe pas")
            }

            val existingArticle = panierRepository.get(userEmail)?.articlesPanier?.find { it.articleId == articleId }
            if (existingArticle == null) {
                // L'article avec articleId n'existe pas dans le panier
                return ResponseEntity.badRequest().body("L'article n'existe pas dans le panier")
            } else {
                val articlePanier = panierRepository.get(userEmail)!!.articlesPanier
                val article = articlePanier.find { it.articleId == articleId }
                articlePanier.remove(article)
                return panierRepository.update(Panier(userEmail, articlePanier)).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }
        }
    }

    @Operation(summary = "Update a panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Panier updated",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/paniers/{userEmail}")
    fun update(@PathVariable userEmail: String, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
            if (userEmail != panier.userEmail) {
                ResponseEntity.badRequest().body("Invalid id")
            } else {
                panierRepository.update(panier.asPanier()).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }

    @Operation(summary = "Delete panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Panier deleted"),
        ApiResponse(responseCode = "400", description = "Panier not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/paniers/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        val deleted = panierRepository.delete(id)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("Panier not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }


    //ACHAT DU PANIER
    @Operation(summary = "Achat du panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Panier acheté",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/paniers/achat/{userEmail}")
    fun achat(@PathVariable userEmail: String): ResponseEntity<Any> {
        val panier = panierRepository.get(userEmail)
        if (panier == null) {
            return ResponseEntity.badRequest().body("Le panier n'existe pas")
        } else {
            val articlesPanier = panier.articlesPanier
            //Si il n'y a pas d'articles dans le panier
            if (articlesPanier.size == 0) {
                return ResponseEntity.badRequest().body("Le panier est vide")
            }
            //Pour chaque article, on revérifie qu'il y a encore la quantité demandée en stock
            //On calcul le prix en même temps pour pas avoir à refaire une boucle
            var prixTotal = 0.0
            for (articlePanier in articlesPanier) {
                val article = httpService.get("articles/${articlePanier.articleId}")
                val articleDTO = ObjectMapper().readValue(article, Article::class.java)
                val quantiteDispo = articleDTO.qteStock
                if (articlePanier.quantite > quantiteDispo) {
                    throw NotEnoughStockError(articlePanier.articleId.toString())
                }
                prixTotal += articleDTO.prix * articlePanier.quantite
            }

            //On décrémente la quantité de chaque article après avoir vérifié qu'il y avait assez de stock pour chaque article
            for (articlePanier in articlesPanier) {
                httpService.decreaseQuantity(articlePanier.articleId, articlePanier.quantite)
            }

            //On met à jour la date de dernière commande du user
            httpService.updateLastOrderDate(userEmail)

            //On vide le panier
            panier.articlesPanier.clear()
            panierRepository.update(panier)

            return ResponseEntity.ok("Le prix total est de $prixTotal €, Merci de votre achat !")
        }
    }



}







class Article(
        val id: Int,
        val nom: String,
        val prix: Double,
        var qteStock: Int,
        var dateMAJ: String
) {
    constructor() : this(0, "", 0.0, 0, "")
}

















