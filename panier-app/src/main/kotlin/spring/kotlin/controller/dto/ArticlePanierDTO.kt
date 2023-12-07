package spring.kotlin.controller.dto

import jakarta.persistence.Id
import spring.kotlin.domain.ArticlePanier

data class ArticlePanierDTO(
        @field:Id val id: Int,
        @field:Id val panierId: Int,
        @field:Id val articleId: Int,
        val quantite: Int
) {
    fun asArticlePanier() = ArticlePanier(this.id, this.panierId, this.articleId, this.quantite)
}

fun ArticlePanier.asArticlePanierDTO() = ArticlePanierDTO(this.id, this.panierId, this.articleId, this.quantite)