package spring.kotlin.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import spring.kotlin.domain.Article
import java.time.LocalDate

@Entity
@Table(name = "articles")
class ArticleEntity(
        @Id val id: Int,
        val nom: String,
        val prix: Float,
        val qteStock: Int,
        val dateMAJ: LocalDate,
) {
    fun asArticle() = Article(this.id, this.nom, this.prix, this.qteStock, this.dateMAJ)
}

fun Article.asEntity() = ArticleEntity(this.id, this.nom, this.prix, this.qteStock, this.dateMAJ)