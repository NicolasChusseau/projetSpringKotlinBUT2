package spring.kotlin.controller.dto

import jakarta.validation.constraints.Email
import spring.kotlin.domain.ArticlePanier

data class ArticlePanierDTO(
        @field:Email val panierId: String,
        val articleId: Int,
        val quantite: Int
) {
    fun asArticlePanier() = ArticlePanier(this.panierId, this.articleId, this.quantite)
}

fun ArticlePanier.asArticlePanierDTO() = ArticlePanierDTO(this.panierId, this.articleId, this.quantite)