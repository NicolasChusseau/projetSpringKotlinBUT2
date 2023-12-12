package spring.kotlin.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import spring.kotlin.domain.Panier

@Entity
@Table(name = "paniers")
class PanierEntity(
    @Id val userEmail: String,
    @OneToMany(mappedBy = "panierId")
        val articlesPanier: MutableList<ArticlePanierEntity>
) {
    fun asPanier() = Panier(this.userEmail, this.articlesPanier.map { it.asArticlePanier() }.toMutableList())
}

fun Panier.asEntity() = PanierEntity(this.userEmail, this.articlesPanier.map { it.asEntity() }.toMutableList())