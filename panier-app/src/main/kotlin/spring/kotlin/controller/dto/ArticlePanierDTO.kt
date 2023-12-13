package spring.kotlin.controller.dto

import spring.kotlin.domain.ArticlePanier

data class ArticlePanierDTO(
        val articleId: Int,
        val quantite: Int
) {
    fun asArticlePanier() = ArticlePanier(this.articleId, this.quantite)
}

fun ArticlePanier.asArticlePanierDTO() = ArticlePanierDTO(this.articleId, this.quantite)