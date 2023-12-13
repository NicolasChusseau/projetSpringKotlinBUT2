package spring.kotlin.controller.dto

import jakarta.validation.constraints.Email
import spring.kotlin.domain.Panier

data class PanierDTO(
        @field:Email val userEmail: String,
        val articlesPanier: MutableList<ArticlePanierDTO>
) {
    fun asPanier() = Panier(this.userEmail, this.articlesPanier.map { it.asArticlePanier() }.toMutableList())
}

fun Panier.asPanierDTO() = PanierDTO(this.userEmail, this.articlesPanier.map { it.asArticlePanierDTO() }.toMutableList())