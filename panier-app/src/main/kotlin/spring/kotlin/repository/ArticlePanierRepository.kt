package spring.kotlin.repository

import spring.kotlin.domain.ArticlePanier


interface ArticlePanierRepository {

    fun create(articlePanier: ArticlePanier): Result<ArticlePanier>
    fun list(): List<ArticlePanier>
    fun get(id: Int): ArticlePanier?
    fun update(articlePanier: ArticlePanier): Result<ArticlePanier>
    fun delete(id: Int): ArticlePanier?
}