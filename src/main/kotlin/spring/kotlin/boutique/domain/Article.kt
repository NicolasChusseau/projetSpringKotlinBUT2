package spring.kotlin.boutique.domain

import java.time.LocalDate

data class Article(val id: Int, val nom: String, val prix: Float, val qteStock: Int, val dateMAJ: LocalDate)
