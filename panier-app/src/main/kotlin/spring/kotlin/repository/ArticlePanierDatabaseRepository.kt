package spring.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.kotlin.domain.ArticlePanier
import spring.kotlin.domain.Panier
import spring.kotlin.repository.entity.ArticlePanierEntity
import spring.kotlin.repository.entity.asEntity
import kotlin.jvm.optionals.getOrNull

class ArticlePanierDatabaseRepository(private val jpa: ArticlePanierJpaRepository) : ArticlePanierRepository {
    override fun create(articlePanier: ArticlePanier): Result<ArticlePanier> = if (jpa.findById(articlePanier.id).isPresent) {
        //TODO("Peut pas créer si l'article est déjà dans ce panier")
        Result.failure(Exception("ArticlePanier already in DB"))
    } else {
        val saved = jpa.save(articlePanier.asEntity())
        Result.success(saved.asArticlePanier())
    }

    override fun list(): List<ArticlePanier> {
        return jpa.findAll().map { it.asArticlePanier() }
    }

    override fun get(id: Int): ArticlePanier? {
        return jpa.findById(id)
            .map { it.asArticlePanier() }
            .getOrNull()
    }

    override fun update(articlePanier: ArticlePanier): Result<ArticlePanier> = if (jpa.findById(articlePanier.id).isPresent) {
        //TODO("Peut juste modifier la quantité")
        val saved = jpa.save(articlePanier.asEntity())
        Result.success(saved.asArticlePanier())
    } else {
        Result.failure(Exception("ArticlePanier not in DB"))
    }

    override fun delete(id: Int): ArticlePanier? {
        return jpa.findById(id)
            .also { jpa.deleteById(id) }
            .map { it.asArticlePanier() }
            .getOrNull()
    }
}


interface ArticlePanierJpaRepository : JpaRepository<ArticlePanierEntity, Int> {
}
