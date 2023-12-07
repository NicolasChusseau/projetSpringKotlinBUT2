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
import spring.kotlin.controller.dto.ArticleDTO
import spring.kotlin.controller.dto.asArticleDTO
import spring.kotlin.errors.ArticleNotFoundError
import spring.kotlin.repository.ArticleRepository
import java.time.LocalDate

@RestController
@Validated
class ArticleController(val articleRepository: ArticleRepository) {
    @Operation(summary = "Create article")
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201", description = "Article created",
                content = [Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleDTO::class)
                )]
        ),
        ApiResponse(responseCode = "409", description = "Article already exist",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/articles")
    fun create(@RequestBody @Valid article: ArticleDTO): ResponseEntity<ArticleDTO> {
        article.dateMAJ = LocalDate.now()
        return articleRepository.create(article.asArticle()).fold(
                { success -> ResponseEntity.status(HttpStatus.CREATED).body(success.asArticleDTO()) },
                { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })
    }

    @Operation(summary = "List articles")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List articles",
                content = [Content(mediaType = "application/json",
                        array = ArraySchema(
                                schema = Schema(implementation = ArticleDTO::class))
                )])])
    @GetMapping("/api/articles")
    fun list() =
            articleRepository.list()
                    .map { it.asArticleDTO() }
                    .let {
                        ResponseEntity.ok(it)
                    }

    @Operation(summary = "Get article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The article",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "404", description = "Article not found")
    ])
    @GetMapping("/api/articles/{id}")
    fun findOne(@PathVariable id: Int): ResponseEntity<ArticleDTO> {
        val article = articleRepository.get(id)
        return if (article != null) {
            ResponseEntity.ok(article.asArticleDTO())
        } else {
            throw ArticleNotFoundError(id)
        }
    }

    @Operation(summary = "Update a article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article updated",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/articles/{id}")
    fun update(@PathVariable id: Int, @RequestBody @Valid article: ArticleDTO): ResponseEntity<Any> =
            if (id != article.id) {
                ResponseEntity.badRequest().body("Invalid id")
            } else {
                article.dateMAJ = LocalDate.now()
                articleRepository.update(article.asArticle()).fold(
                        { success -> ResponseEntity.ok(success.asArticleDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }

    @Operation(summary = "Delete article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Article deleted"),
        ApiResponse(responseCode = "400", description = "Article not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/articles/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Any> {
        val deleted = articleRepository.delete(id)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("Article not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }
}