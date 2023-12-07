package spring.kotlin.boutique.repository

import spring.kotlin.boutique.domain.Article

interface ArticleRepository {
    fun create(article: Article): Result<Article>
    fun list(): List<Article>
    fun get(id: Int): Article?
    fun update(article: Article): Result<Article>
    fun delete(id: Int): Article?
}