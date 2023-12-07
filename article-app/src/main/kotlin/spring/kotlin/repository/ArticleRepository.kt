package spring.kotlin.repository

import spring.kotlin.domain.Article

interface ArticleRepository {
    fun create(article: Article): Result<Article>
    fun list(): List<Article>
    fun get(id: Int): Article?
    fun update(article: Article): Result<Article>
    fun delete(id: Int): Article?
}