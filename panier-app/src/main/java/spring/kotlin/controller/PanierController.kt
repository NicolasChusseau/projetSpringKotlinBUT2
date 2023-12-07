package spring.kotlin.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.kotlin.controller.dto.PanierDTO
import spring.kotlin.controller.dto.asPanierDTO
import spring.kotlin.errors.PanierNotFoundError
import spring.kotlin.repository.PanierRepository


@RestController
@Validated
class PanierController(val panierRepository: PanierRepository) {
    @Operation(summary = "Create user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Panier created",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class)
                )]),
        ApiResponse(responseCode = "409", description = "Panier already exist",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/users")
    fun create(@RequestBody @Valid user: PanierDTO): ResponseEntity<PanierDTO> =
            panierRepository.create(user.asPanier()).fold(
                    { success -> ResponseEntity.status(HttpStatus.CREATED).body(success.asPanierDTO()) },
                    { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })



    @Operation(summary = "List users")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List users",
                content = [Content(mediaType = "application/json",
                        array = ArraySchema(
                                schema = Schema(implementation = PanierDTO::class))
                )])])
    @GetMapping("/api/users")
    fun list() =
            panierRepository.list()
                    .map { it.asPanierDTO() }
                    .let {
                        ResponseEntity.ok(it)
                    }

    @Operation(summary = "Get user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The user",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @GetMapping("/api/users/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<PanierDTO> {
        val user = panierRepository.get(email)
        return if (user != null) {
            ResponseEntity.ok(user.asPanierDTO())
        } else {
            throw PanierNotFoundError(email)
        }
    }

    @Operation(summary = "Update a user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Panier updated",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/users/{email}")
    fun update(@PathVariable @Email email: String, @RequestBody @Valid user: PanierDTO): ResponseEntity<Any> =
            if (email != user.email) {
                ResponseEntity.badRequest().body("Invalid email")
            } else {
                panierRepository.update(user.asPanier()).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }

    @Operation(summary = "Delete user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Panier deleted"),
        ApiResponse(responseCode = "400", description = "Panier not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/users/{email}")
    fun delete(@PathVariable @Email email: String): ResponseEntity<Any> {
        val deleted = panierRepository.delete(email)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("Panier not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }

}

























