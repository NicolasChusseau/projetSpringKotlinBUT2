package spring.kotlin.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import spring.kotlin.domain.User
import java.time.LocalDate

data class UserDTO(
    @field:Email val email: String,
    val nom: String,
    val adresseDeLivraison: String,
    val estAbonnee: Boolean,
    val dateDerniereCommande: LocalDate?,
    @field:Min(15) @field:Max(120) val age: Int
) {
    fun asUser() =
        User(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)
}

fun User.asUserDTO() =
    UserDTO(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)