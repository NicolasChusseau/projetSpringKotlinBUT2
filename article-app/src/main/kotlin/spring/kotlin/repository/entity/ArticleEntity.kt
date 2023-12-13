package spring.kotlin.repository.entity

import jakarta.persistence.*
import spring.kotlin.domain.Article
import java.time.LocalDate

@Entity
@Table(name = "articles")
class ArticleEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id val id: Int,
        val nom: String,
        val prix: Float,
        val qteStock: Int,
        @Column(name = "date_maj")
        var dateMAJ: LocalDate = LocalDate.now()
) {
    fun asArticle() = Article(this.id, this.nom, this.prix, this.qteStock, this.dateMAJ)
}

fun Article.asEntity() = ArticleEntity(this.id, this.nom, this.prix, this.qteStock, this.dateMAJ)