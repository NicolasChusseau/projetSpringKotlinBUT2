package spring.kotlin.domain

import java.time.LocalDate

data class Article(val id: Int, val nom: String, val prix: Float, var qteStock: Int, var dateMAJ: LocalDate)
