package spring.kotlin.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import spring.kotlin.domain.User
import java.time.LocalDate

@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        val nom : String,
        val adresseDeLivraison: String,
        val estAbonnee: Boolean,
        val dateDerniereCommande: LocalDate?,
        val age: Int
) {
    fun asUser() = User(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)
}
fun User.asEntity() = UserEntity(this.email, this.nom, this.adresseDeLivraison, this.estAbonnee, this.dateDerniereCommande, this.age)