package spring.kotlin.repository

import assertk.assertions.*
import assertk.assertThat
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spring.kotlin.domain.User

@DataJpaTest

class UserDatabaseRepositoryTest {
    @MockkBean
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var jpa: UserJpaRepository

    @BeforeEach
    fun setUp() {
        repository = UserDatabaseRepository(jpa)
    }

    @Nested
    inner class Create {
        @Test
        fun `should create an user`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            // When
            val result = repository.create(user)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(user)
        }

        @Test
        fun `should not create an user if already in DB`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            repository.create(user)
            // When
            val result = repository.create(user)
            // Then
            assertThat(result).isFailure()
        }

        @Test
        fun `should create an user with id 2`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            // When
            val result = repository.create(user)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(user)
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should list all users`() {
            // Given
            val user1 = User("email1", "first1", "last1", true, null, 15)
            val user2 = User("email2", "first2", "last2", true, null, 15)
            repository.create(user1)
            repository.create(user2)
            // When
            val result = repository.list()
            // Then
            assertThat(result).containsExactlyInAnyOrder(user1, user2)
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `should get an user`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            repository.create(user)
            // When
            val result = repository.get("email")
            // Then
            assertThat(result).isEqualTo(user)
        }

        @Test
        fun `should not get an user if not in DB`() {
            // Given
            // Nothing
            // When
            val result = repository.get("cc")
            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class Update {
        @Test
        fun `should update an user`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            repository.create(user)
            val updatedUser = User("email", "first", "last", true, null, 15)
            // When
            val result = repository.update(updatedUser)
            // Then
            assertThat(result).isSuccess()
                    .isEqualTo(updatedUser)
        }

        @Test
        fun `should not update an user if not in DB`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            // When
            val result = repository.update(user)
            // Then
            assertThat(result).isFailure()
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete an user`() {
            // Given
            val user = User("email", "first", "last", true, null, 15)
            repository.create(user)
            // When
            val result = repository.delete("email")
            // Then
            assertThat(result).isEqualTo(user)
        }

        @Test
        fun `should not delete an user if not in DB`() {
            // When
            val result = repository.delete("email")
            // Then
            assertThat(result).isNull()
        }
    }

}












