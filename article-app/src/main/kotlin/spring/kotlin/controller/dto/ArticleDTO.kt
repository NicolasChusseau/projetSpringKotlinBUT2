package spring.kotlin.controller.dto

import org.springframework.data.annotation.Id
import spring.kotlin.domain.Article
import java.time.LocalDate

data class ArticleDTO(
    @field:Id val id: Int,
    val nom: String,
    val prix: Float,
    val qteStock: Int,
    val dateMAJ: LocalDate
) {

    fun asArticle() = Article(id, nom, prix, qteStock, dateMAJ)
}

fun Article.asArticleDTO() = ArticleDTO(this.id, this.nom, this.prix, this.qteStock, this.dateMAJ)