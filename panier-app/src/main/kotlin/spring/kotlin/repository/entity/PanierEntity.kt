package spring.kotlin.repository.entity

import jakarta.persistence.*
import spring.kotlin.domain.ArticlePanier
import spring.kotlin.domain.Panier

@Entity
@Table(name = "paniers")
class PanierEntity(
        @Id
        @Column(name = "user_email")
        val userEmail: String,

        @ElementCollection
        @CollectionTable(name = "article_panier", joinColumns = [JoinColumn(name = "panier_id")])
        val articlesPanier: MutableList<ArticlePanierEmbeddable> = mutableListOf()
) {
    fun asPanier() = Panier(this.userEmail, this.articlesPanier.map { it.asArticlePanier() }.toMutableList())
}

fun Panier.asEntity() = PanierEntity(this.userEmail, this.articlesPanier.map { ArticlePanierEmbeddable(it.articleId, it.quantite) }.toMutableList())

@Embeddable
class ArticlePanierEmbeddable(
        val articleId: Int,
        val quantite: Int
) {
    fun asArticlePanier() = ArticlePanier(articleId, quantite)
}
















