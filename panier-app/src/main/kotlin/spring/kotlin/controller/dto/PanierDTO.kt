package spring.kotlin.controller.dto

import jakarta.persistence.Id
import spring.kotlin.domain.Panier

data class PanierDTO(
        @field:Id val id: Int,
        @field:Id val userId: Int,
        //Une liste de produits
        val articlesPanier: List<ArticlePanierDTO>
) {
    fun asPanier() = Panier(this.id, this.userId, this.articlesPanier.map { it.asArticlePanier() })
}

fun Panier.asPanierDTO() = PanierDTO(this.id, this.userId, this.articlesPanier.map { it.asArticlePanierDTO() })