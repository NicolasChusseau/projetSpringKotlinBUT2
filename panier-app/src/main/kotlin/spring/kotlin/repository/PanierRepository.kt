package spring.kotlin.repository

import spring.kotlin.domain.Panier


interface PanierRepository {

    fun create(panier: Panier): Result<Panier>
    fun list(): List<Panier>
    fun get(userId: String): Panier?
    fun update(panier: Panier): Result<Panier>
    fun delete(userId: String): Panier?
}