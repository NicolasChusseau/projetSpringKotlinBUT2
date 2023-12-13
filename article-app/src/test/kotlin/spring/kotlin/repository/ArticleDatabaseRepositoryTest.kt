package spring.kotlin.repository

import assertk.assertThat
import assertk.assertions.*
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spring.kotlin.domain.Article
import java.time.LocalDate


@DataJpaTest
class ArticleDatabaseRepositoryTest {
    @MockkBean
    private lateinit var repository: ArticleRepository

    @Autowired
    private lateinit var jpa: ArticleJpaRepository

    @BeforeEach
    fun setUp() {
        jpa.deleteAll()
        repository = ArticleDatabaseRepository(jpa)
    }

    @Nested
    inner class Create {
        @Test
        fun `should create an article`() {
            // Given
            val article = Article(1, "Article 1", 10.0.toFloat(), 10, LocalDate.now())
            // When
            val result = repository.create(article)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(article)
        }

        @Test
        fun `should create an article with id 2`() {
            // Given
            val article = Article(2, "Article 2", 10.0.toFloat(), 10, LocalDate.now())
            // When
            val result = repository.create(article)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(article)
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should list articles`() {
            // Given
            val article1 = defaultArticle()
            val article2 = defaultArticle(nom="Article 2")
            val res1 = repository.create(article1)
            val res2 = repository.create(article2)
            // When
            val result = repository.list()
            val id1 = res1.getOrNull()!!.id
            val id2 = res2.getOrNull()!!.id
            // Then
            assertThat(result).containsExactlyInAnyOrder(defaultArticle(id=id1), defaultArticle(id=id2, nom="Article 2"))
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `should get an article by id`() {
            // Given
            val article = defaultArticle()
            val res = repository.create(article)
            //On récupère l'id de l'article créé car il est auto-incrémenté
            val id = res.getOrNull()!!.id
            // When
            val result = repository.get(id)
            // Then
            assertThat(result).isEqualTo(defaultArticle(id=id))
        }

        @Test
        fun `should return null if article not found`() {
            // When
            val result = repository.get(1)
            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class Update {
        @Test
        fun `should not update an article if not in DB`() {
            // Given
            val updatedArticle = defaultArticle(nom="Article 1.1")
            // When
            val result = repository.update(updatedArticle)
            // Then
            assertThat(result).isFailure()
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete an article`() {
            // Given
            val article = defaultArticle()
            val res = repository.create(article)
            //On récupère l'id de l'article créé car il est auto-incrémenté
            val id = res.getOrNull()!!.id
            // When
            val result = repository.delete(id)
            // Then
            assertThat(result).isEqualTo(defaultArticle(id=id)) //On est obligé de mettre l'id car il est auto-incrémenté, sinon ça ne marche pas
        }

        @Test
        fun `should return null if article not found`() {
            // When
            val result = repository.delete(666)
            // Then
            assertThat(result).isNull()
        }
    }


    private fun defaultArticle(
            id: Int =1,
            nom: String ="default one",
            prix: Float =10.0.toFloat(),
            quantite: Int =10,
            dateMAJ: LocalDate =LocalDate.now()
    ) = Article(id, nom, prix, quantite, dateMAJ)
}