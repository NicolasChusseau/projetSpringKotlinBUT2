package spring.kotlin.controller.dto

import spring.kotlin.domain.Article
import java.time.LocalDate

data class ArticleDTO(
        val nom: String,
        val prix: Float,
        val qteStock: Int
) {

    fun asArticle() = Article(0, this.nom, this.prix, this.qteStock, LocalDate.now())
}

fun Article.asArticleDTO() = ArticleDTO(this.nom, this.prix, this.qteStock)