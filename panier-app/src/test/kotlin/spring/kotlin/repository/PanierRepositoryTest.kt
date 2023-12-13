package spring.kotlin.repository

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import spring.kotlin.domain.Panier

abstract class PanierRepositoryTest {
    protected lateinit var repository: PanierRepository

    @Nested
    inner class Create {
        @Test
        fun `should create a panier`() {
            // Given
            val panier = Panier("email", mutableListOf())
            // When
            val result = repository.create(panier)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(panier)
        }

        @Test
        fun `should not create a panier if already in DB`() {
            // Given
            val panier = Panier("email", mutableListOf())
            repository.create(panier)
            // When
            val result = repository.create(panier)
            // Then
            assertThat(result).isFailure()
        }

        @Test
        fun `should create a panier with id 2`() {
            // Given
            val panier = Panier("email", mutableListOf())
            // When
            val result = repository.create(panier)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(panier)
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should list paniers`() {
            // Given
            val panier1 = Panier("email1", mutableListOf())
            val panier2 = Panier("email2", mutableListOf())
            repository.create(panier1)
            repository.create(panier2)
            // When
            val result = repository.list()
            // Then
            assertThat(result).containsExactlyInAnyOrder(panier1, panier2)
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `should get a panier by id`() {
            // Given
            val panier = Panier("email", mutableListOf())
            repository.create(panier)
            // When
            val result = repository.get("email")
            // Then
            assertThat(result).isEqualTo(panier)
        }

        @Test
        fun `should not get a panier by id if not in DB`() {
            // Given
            val panier = Panier("email", mutableListOf())
            repository.create(panier)
            // When
            val result = repository.get("email2")
            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class Update {
        @Test
        fun `should update a panier`() {
            // Given
            val panier = Panier("email", mutableListOf())
            repository.create(panier)
            // When
            val result = repository.update(panier)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(panier)
        }

        @Test
        fun `should not update a panier if not in DB`() {
            // Given
            val panier = Panier("email", mutableListOf())
            // When
            val result = repository.update(panier)
            // Then
            assertThat(result).isFailure()
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete a panier by id`() {
            // Given
            val panier = Panier("email", mutableListOf())
            repository.create(panier)
            // When
            val result = repository.delete("email")
            // Then
            assertThat(result).isEqualTo(panier)
        }

        @Test
        fun `should not delete a panier by id if not in DB`() {
            // Given
            val panier = Panier("email", mutableListOf())
            repository.create(panier)
            // When
            val result = repository.delete("email2")
            // Then
            assertThat(result).isNull()
        }
    }

}





















