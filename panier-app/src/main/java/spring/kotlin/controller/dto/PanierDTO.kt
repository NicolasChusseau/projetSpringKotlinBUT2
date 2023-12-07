package spring.kotlin.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import spring.kotlin.domain.Panier
import java.time.LocalDate

data class PanierDTO(
        @field:Email val email: String,
        val nom: String,
        val adresseDeLivraison: String,
        val estAbonnee: Boolean,
        val dateDerniereCommande: LocalDate?,
        @field:Min(15) @field:Max(120) val age: Int
) {
        fun asPanier() = Panier(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)
}

fun Panier.asPanierDTO() = PanierDTO(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)