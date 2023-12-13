package spring.kotlin.repository

import org.junit.jupiter.api.BeforeEach


class PanierInMemoryRepositoryTest : PanierRepositoryTest() {

    @BeforeEach
    fun setUp() {
        repository = PanierInMemoryRepository()
    }
}