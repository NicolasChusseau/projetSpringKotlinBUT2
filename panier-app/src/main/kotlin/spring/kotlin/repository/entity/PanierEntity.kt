package spring.kotlin.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import spring.kotlin.domain.Panier
import java.time.LocalDate

@Entity
@Table(name = "users")
class PanierEntity(
        @Id val email: String,
        val nom : String,
        val adresseDeLivraison: String,
        val estAbonnee: Boolean,
        val dateDerniereCommande: LocalDate?,
        val age: Int
) {
    fun asPanier() = Panier(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)
}
fun Panier.asEntity() = PanierEntity(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)