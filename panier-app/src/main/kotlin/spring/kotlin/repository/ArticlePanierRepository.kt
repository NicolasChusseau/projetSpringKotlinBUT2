package spring.kotlin.repository

import org.springframework.stereotype.Repository
import spring.kotlin.domain.ArticlePanier
import spring.kotlin.domain.Panier

@Repository
interface ArticlePanierRepository {

    fun create(articlePanier: ArticlePanier): Result<ArticlePanier>
    fun list(): List<ArticlePanier>
    fun get(id: Int): ArticlePanier?
    fun update(articlePanier: ArticlePanier): Result<ArticlePanier>
    fun delete(id: Int): ArticlePanier?
}