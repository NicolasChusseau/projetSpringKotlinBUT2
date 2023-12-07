package spring.kotlin.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spring.kotlin.repository.PanierDatabaseRepository
import spring.kotlin.repository.PanierInMemoryRepository
import spring.kotlin.repository.PanierJpaRepository

@Configuration
class DatabaseConfig {
    @ConditionalOnProperty(
            "db.external",
            havingValue = "false",
            matchIfMissing = true
    )
    @Bean
    fun inMemory() = PanierInMemoryRepository()

    @ConditionalOnProperty(
            "db.external",
            havingValue = "true",
            matchIfMissing = false
    )
    @Bean
    fun inDatabase(jpa: PanierJpaRepository) = PanierDatabaseRepository(jpa)
}