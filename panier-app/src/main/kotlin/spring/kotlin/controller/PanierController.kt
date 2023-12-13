package spring.kotlin.controller

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
import spring.kotlin.errors.PanierNotFoundError
import spring.kotlin.repository.PanierRepository


@RestController
@Validated
class PanierController(val panierRepository: PanierRepository) {
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

    @Operation(summary = "Add panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @GetMapping("/api/paniers/addToPanier/{userEmail}/{articleId}/{quantite}")
    fun addArticlePanier(@PathVariable userEmail: String, @PathVariable articleId: Int, @PathVariable quantite: Int, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
        if (userEmail != panier.userEmail) {
            ResponseEntity.badRequest().body("Invalid id")
        } else {
            val newArticle = articlePanier.create(ArticlePanier(userEmail, articleId, quantite))
            val panierActuel = panierRepository.get(userEmail)
            panierActuel?.articlesPanier?.add(newArticle.getOrNull()!!)
            panierRepository.update(panier.asPanier()).fold(
                { success -> ResponseEntity.ok(success.asPanierDTO()) },
                { failure -> ResponseEntity.badRequest().body(failure.message) }
            )
        }

    //TODO
    @Operation(summary = "Add quantity panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @PostMapping("/api/paniers/addToPanier/{userEmail}/{articleId}/{quantite}")
    fun addQuantityArticlePanier(
        @PathVariable userEmail: String,
        @PathVariable articleId: Int,
        @PathVariable quantite: Int
    ): ResponseEntity<Any> =
        if (panierRepository.get(userEmail) == null) {
            ResponseEntity.badRequest().body("Invalid id")
        } else {
            val existingArticle = panierRepository.get(userEmail)?.articlesPanier?.find { it.articleId == articleId }

            if (existingArticle == null) {
                // L'article avec articleId n'existe pas dans le panier
                val newArticle = articlePanier.create(ArticlePanier(userEmail, articleId, quantite))
                val panierActuel = panierRepository.get(userEmail)
                panierActuel?.articlesPanier?.add(newArticle.getOrNull()!!)
                panierRepository.update(panier.asPanier()).fold(
                    { success -> ResponseEntity.ok(success.asPanierDTO()) },
                    { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            } else {
                // L'article avec articleId existe déjà dans le panier
                // Faites quelque chose en conséquence
                ResponseEntity.badRequest().body("L'article avec l'ID $articleId existe déjà dans le panier.")
            }
        }


    //TODO
    @Operation(summary = "Remove panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @GetMapping("/api/paniers/addToPanier/{userEmail}/{articleId}/{quantite}")
    fun removeArticlePanier(@PathVariable userEmail: String, @PathVariable articleId: Int, @PathVariable quantite: Int, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
        if (userEmail != panier.userEmail) {
            ResponseEntity.badRequest().body("Invalid id")
        } else {
            val newArticle = articlePanier.create(ArticlePanier(userEmail, articleId, quantite))
            val panierActuel = panierRepository.get(userEmail)
            panierActuel?.articlesPanier?.add(newArticle.getOrNull()!!)
            panierRepository.update(panier.asPanier()).fold(
                { success -> ResponseEntity.ok(success.asPanierDTO()) },
                { failure -> ResponseEntity.badRequest().body(failure.message) }
            )
        }


    //TODO
    @Operation(summary = "Remove quantity panierArticle by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @GetMapping("/api/paniers/addToPanier/{userEmail}/{articleId}/{quantite}")
    fun removeQuantityArticlePanier(@PathVariable userEmail: String, @PathVariable articleId: Int, @PathVariable quantite: Int, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
        if (userEmail != panier.userEmail) {
            ResponseEntity.badRequest().body("Invalid id")
        } else {
            val newArticle = articlePanier.create(ArticlePanier(userEmail, articleId, quantite))
            val panierActuel = panierRepository.get(userEmail)
            panierActuel?.articlesPanier?.add(newArticle.getOrNull()!!)
            panierRepository.update(panier.asPanier()).fold(
                { success -> ResponseEntity.ok(success.asPanierDTO()) },
                { failure -> ResponseEntity.badRequest().body(failure.message) }
            )
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

}

























