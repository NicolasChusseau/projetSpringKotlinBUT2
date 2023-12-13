package spring.kotlin.repository

import spring.kotlin.domain.Panier

class PanierInMemoryRepository : PanierRepository {
    private val paniers = mutableMapOf<String, Panier>()

    override fun create(panier: Panier): Result<Panier> = if (paniers.containsKey(panier.userEmail)) {
        Result.failure(Exception("Panier already in DB"))
    } else {
        paniers[panier.userEmail] = panier
        Result.success(panier)
    }

    override fun list(): List<Panier> {
        return paniers.values.toList()
    }

    override fun get(userId: String): Panier? {
        return paniers[userId]
    }

    override fun update(panier: Panier): Result<Panier> = if (paniers.containsKey(panier.userEmail)) {
        paniers[panier.userEmail] = panier
        Result.success(panier)
    } else {
        Result.failure(Exception("Panier not in DB"))
    }

    override fun delete(userId: String): Panier? {
        return paniers.remove(userId)
    }
}