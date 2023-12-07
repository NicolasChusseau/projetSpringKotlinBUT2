package spring.kotlin.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import spring.kotlin.domain.Panier

@Entity
@Table(name = "paniers")
class PanierEntity(
        @Id val id: Int,
        val userId: Int,
        @OneToMany(mappedBy = "panierId")
        val articlesPanier: List<ArticlePanierEntity>
) {
    fun asPanier() = Panier(this.id, this.userId, this.articlesPanier.map { it.asArticlePanier() })
}

fun Panier.asEntity() = PanierEntity(this.id, this.userId, this.articlesPanier.map { it.asEntity() })