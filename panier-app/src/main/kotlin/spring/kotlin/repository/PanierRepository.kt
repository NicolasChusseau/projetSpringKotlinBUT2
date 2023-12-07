package spring.kotlin.repository

import org.springframework.stereotype.Repository
import spring.kotlin.domain.Panier

@Repository
interface PanierRepository {

    fun create(panier: Panier): Result<Panier>
    fun list(): List<Panier>
    fun get(id: Int): Panier?
    fun update(panier: Panier): Result<Panier>
    fun delete(id: Int): Panier?
}