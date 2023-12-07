package spring.kotlin.boutique.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.kotlin.boutique.domain.Article
import spring.kotlin.boutique.repository.entity.ArticleEntity
import spring.kotlin.boutique.repository.entity.asEntity
import kotlin.jvm.optionals.getOrNull

@Repository
class ArticleDatabaseRepository(private val jpa: ArticleJpaRepository) : ArticleRepository {
    override fun create(article: Article): Result<Article> = if (jpa.findById(article.id).isPresent) {
        Result.failure(Exception("Article already in DB"))
    } else {
        val saved = jpa.save(article.asEntity())
        Result.success(saved.asArticle())
    }

    override fun list(): List<Article> {
        return jpa.findAll().map { it.asArticle() }
    }

    override fun get(id: Int): Article? {
        return jpa.findById(id)
            .map { it.asArticle() }
            .getOrNull()
    }

    override fun update(article: Article): Result<Article> = if (jpa.findById(article.id).isPresent) {
        val saved = jpa.save(article.asEntity())
        Result.success(saved.asArticle())
    } else {
        Result.failure(Exception("Article not in DB"))
    }

    override fun delete(id: Int): Article? {
        return jpa.findById(id)
            .also { jpa.deleteById(id) }
            .map { it.asArticle() }
            .getOrNull()
    }

}

interface ArticleJpaRepository : JpaRepository<ArticleEntity, Int> {
}