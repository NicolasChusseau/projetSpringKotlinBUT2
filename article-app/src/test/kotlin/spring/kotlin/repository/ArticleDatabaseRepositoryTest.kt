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
        fun `should not create an article if already in DB`() {
            // Given
            val article = Article(1, "Article 1.1", 10.0.toFloat(), 10, LocalDate.now())
            repository.create(article)
            // When
            val result = repository.create(article)
            // Then
            assertThat(result).isFailure()
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
            val article1 = Article(1, "Article 1", 10.0.toFloat(), 10, LocalDate.now())
            val article2 = Article(2, "Article 2", 10.0.toFloat(), 10, LocalDate.now())
            repository.create(article1)
            repository.create(article2)
            // When
            val result = repository.list()
            // Then
            assertThat(result).containsExactlyInAnyOrder(article1, article2)
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `should get an article by id`() {
            // Given
            val article = defaultArticle()
            repository.create(article)
            // When
            val result = repository.get(0)
            // Then
            assertThat(result).isEqualTo(article)
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
        fun `should update an article`() {
            // Given
            val article = defaultArticle()
            repository.create(article)
            val updatedArticle = defaultArticle(nom="Article 1.1")
            // When
            val result = repository.update(updatedArticle)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(updatedArticle)
        }

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
            repository.create(article)
            // When
            val result = repository.delete(0)
            // Then
            assertThat(result).isEqualTo(article)
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
            id: Int =0,
            nom: String ="default one",
            prix: Float =10.0.toFloat(),
            quantite: Int =10,
            dateMAJ: LocalDate =LocalDate.now()
    ) = Article(id, nom, prix, quantite, dateMAJ)
}