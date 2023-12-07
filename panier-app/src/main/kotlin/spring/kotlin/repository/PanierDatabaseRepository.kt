package spring.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.kotlin.domain.Panier
import spring.kotlin.repository.entity.PanierEntity
import spring.kotlin.repository.entity.asEntity
import kotlin.jvm.optionals.getOrNull

class PanierDatabaseRepository(private val jpa: PanierJpaRepository) : PanierRepository {
    override fun create(panier: Panier): Result<Panier> = if (jpa.findById(panier.id).isPresent) {
        Result.failure(Exception("Panier already in DB"))
    } else {
        val saved = jpa.save(panier.asEntity())
        Result.success(saved.asPanier())
    }

    override fun list(): List<Panier> {
        return jpa.findAll().map { it.asPanier() }
    }

    override fun get(id: Int): Panier? {
        return jpa.findById(id)
            .map { it.asPanier() }
            .getOrNull()
    }

    override fun update(panier: Panier): Result<Panier> = if (jpa.findById(panier.id).isPresent) {
        val saved = jpa.save(panier.asEntity())
        Result.success(saved.asPanier())
    } else {
        Result.failure(Exception("Panier not in DB"))
    }

    override fun delete(id: Int): Panier? {
        return jpa.findById(id)
            .also { jpa.deleteById(id) }
            .map { it.asPanier() }
            .getOrNull()
    }
}


interface PanierJpaRepository : JpaRepository<PanierEntity, Int> {
}
