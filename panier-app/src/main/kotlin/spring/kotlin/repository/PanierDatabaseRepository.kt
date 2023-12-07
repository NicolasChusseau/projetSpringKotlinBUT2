package spring.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.kotlin.domain.Panier
import spring.kotlin.repository.entity.PanierEntity

class PanierDatabaseRepository(private val jpa: PanierJpaRepository) : PanierRepository {
    override fun create(user: Panier): Result<Panier> {
        TODO("Not yet implemented")
    }

    override fun list(age: Int?): List<Panier> {
        TODO("Not yet implemented")
    }

    override fun get(email: String): Panier? {
        TODO("Not yet implemented")
    }

    override fun update(user: Panier): Result<Panier> {
        TODO("Not yet implemented")
    }

    override fun delete(email: String): Panier? {
        TODO("Not yet implemented")
    }
}


interface PanierJpaRepository : JpaRepository<PanierEntity, String> {
}
