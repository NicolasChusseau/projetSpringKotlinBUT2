package spring.kotlin.boutique.domain

import java.time.LocalDate

data class User(
    var email: String,
    var nom: String,
    var adresseDeLivraison: String,
    var estAbonnee: Boolean,
    var dateDerniereCommande: LocalDate?,
    var age: Int
) {
}