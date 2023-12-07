package spring.kotlin.repository

import org.springframework.stereotype.Repository
import spring.kotlin.domain.Panier

@Repository
interface PanierRepository {

    fun create(user: Panier): Result<Panier>
    fun list(age: Int? = null): List<Panier>
    fun get(email: String): Panier?
    fun update(user: Panier): Result<Panier>
    fun delete(email: String): Panier?
}