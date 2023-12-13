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
import spring.kotlin.controller.dto.UserDTO
import spring.kotlin.controller.dto.asUserDTO
import spring.kotlin.domain.User
import spring.kotlin.errors.CannotCreatedPanierError
import spring.kotlin.errors.UserNotFoundError
import spring.kotlin.repository.UserRepository
import spring.kotlin.service.HttpService


@RestController
@Validated
class UserController(
        val userRepository: UserRepository,
        val httpService: HttpService
) {

    @Operation(summary = "Create user")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "201", description = "User created",
                        content = [Content(
                                mediaType = "application/json",
                                schema = Schema(implementation = UserDTO::class)
                        )]
                ),
                ApiResponse(
                        responseCode = "409", description = "User already exist",
                        content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
                )]
    )
    @PostMapping("/api/users")
    fun create(@RequestBody @Valid user: UserDTO): ResponseEntity<UserDTO> {
        val res = userRepository.create(user.asUser()).fold(
                { success ->
                    // Create panier associated to user
                    try {
                        httpService.post("paniers", user.email)
                    } catch (e: Exception) {
                        //On n'a pas pu créer le panier, on supprime l'utilisateur
                        userRepository.delete(user.email)
                        throw CannotCreatedPanierError(user.email)
                    }
                    ResponseEntity.status(HttpStatus.CREATED).body(success.asUserDTO())
                },
                { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })
        return res
    }

    @Operation(summary = "List users")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200", description = "List users",
                        content = [Content(
                                mediaType = "application/json",
                                array = ArraySchema(
                                        schema = Schema(implementation = User::class)
                                )
                        )]
                )]
    )
    @GetMapping("/api/users")
    fun list() =
            userRepository.list()
                    .let {
                        ResponseEntity.ok(it)
                    }

    @Operation(summary = "Get user by email")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200", description = "The user",
                        content = [
                            Content(
                                    mediaType = "application/json",
                                    schema = Schema(implementation = User::class)
                            )]
                ),
                ApiResponse(responseCode = "404", description = "User not found")
            ]
    )
    @GetMapping("/api/users/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<User> {
        val user = userRepository.get(email)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            throw UserNotFoundError(email)
        }
    }

    @Operation(summary = "Update a user by email")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200", description = "User updated",
                        content = [Content(
                                mediaType = "application/json",
                                schema = Schema(implementation = User::class)
                        )]
                ),
                ApiResponse(
                        responseCode = "400", description = "Invalid request",
                        content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
                )]
    )
    @PutMapping("/api/users/{email}")
    fun update(@PathVariable @Email email: String, @RequestBody @Valid user: User): ResponseEntity<Any> =
            if (email != user.email) {
                ResponseEntity.badRequest().body("Invalid email")
            } else {
                userRepository.update(user).fold(
                        { success -> ResponseEntity.ok(success.asUserDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }

    @Operation(summary = "Delete user by email")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "204", description = "User deleted"),
                ApiResponse(
                        responseCode = "400", description = "User not found",
                        content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
                )
            ]
    )
    @DeleteMapping("/api/users/{email}")
    fun delete(@PathVariable @Email email: String): ResponseEntity<Any> {
        val deleted = userRepository.delete(email)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("User not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }

}

























