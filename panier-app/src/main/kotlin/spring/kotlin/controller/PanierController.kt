package spring.kotlin.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.data.annotation.Id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.kotlin.controller.dto.PanierDTO
import spring.kotlin.controller.dto.asPanierDTO
import spring.kotlin.domain.ArticlePanier
import spring.kotlin.errors.PanierNotFoundError
import spring.kotlin.repository.ArticlePanierRepository
import spring.kotlin.repository.PanierRepository


@RestController
@Validated
class PanierController(val panierRepository: PanierRepository, val articlePanier: ArticlePanierRepository) {
    @Operation(summary = "Create panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Panier created",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class)
                )]),
        ApiResponse(responseCode = "409", description = "Panier already exist",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/paniers")
    fun create(@RequestBody @Valid panier: PanierDTO): ResponseEntity<PanierDTO> =
            panierRepository.create(panier.asPanier()).fold(
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
    fun findOne(@PathVariable id: Int): ResponseEntity<PanierDTO> {
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
    @GetMapping("/api/paniers/addToPanier/{panierId}/{articleId}/{quantite}")
    fun addArticlePanier(@PathVariable panierId: Int, @PathVariable articleId: Int, @PathVariable quantite: Int, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
        if (panierId != panier.id) {
            ResponseEntity.badRequest().body("Invalid id")
        } else {
            val newArticle = articlePanier.create(ArticlePanier(panierId, articleId, quantite))
            val panierActuel = panierRepository.get(panierId)
            //TODO : change to mutable list
            //panierActuel.articlesPanier.add()
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
    @PutMapping("/api/paniers/{id}")
    fun update(@PathVariable id: Int, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
            if (id != panier.id) {
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
    fun delete(@PathVariable id: Int): ResponseEntity<Any> {
        val deleted = panierRepository.delete(id)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("Panier not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }

}

























