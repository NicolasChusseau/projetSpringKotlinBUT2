package spring.kotlin.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest
class PanierDatabaseRepositoryTest : PanierRepositoryTest() {

    @Autowired
    private lateinit var jpa: PanierJpaRepository

    @BeforeEach
    fun setUp() {
        repository = PanierDatabaseRepository(jpa)
        jpa.deleteAll()
    }
}