package spring.kotlin.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import spring.kotlin.controller.dto.ArticlePanierDTO
import spring.kotlin.controller.dto.PanierDTO
import spring.kotlin.domain.ArticlePanier
import spring.kotlin.domain.Panier
import spring.kotlin.repository.PanierRepository
import spring.kotlin.service.HttpService


@SpringBootTest
class PanierControllerTest {
    @MockkBean
    lateinit var panierRepository: PanierRepository

    @MockkBean
    lateinit var httpService: HttpService

    @Autowired
    lateinit var panierController: PanierController

    @Nested
    inner class UpdateTests {
        @Test
        fun `add article to panier`() {
            // GIVEN
            val initialPanier = Panier("email", mutableListOf())
            val updatedPanier = Panier("email", mutableListOf(ArticlePanier(1, 3)))
            every { panierRepository.update(any()) } returns Result.success(updatedPanier)
            every { panierRepository.get(any()) } returns initialPanier
            every { httpService.get(any()) } returns "{\n" +  // --> {"id": 1, "nom": "string", "prix": 0, "qteStock": 10, "dateMAJ": "2023-12-13"}
                    "    \"id\": 1,\n" +
                    "    \"nom\": \"string\",\n" +
                    "    \"prix\": 0,\n" +
                    "    \"qteStock\": 100,\n" +
                    "    \"dateMAJ\": \"2023-12-13\"\n" +
                    "}"
            // WHEN
            val result = panierController.addQuantityArticlePanier("email", 1, 3)
            // THEN
            assertThat(result).isEqualTo(ResponseEntity.ok(PanierDTO("email", mutableListOf(ArticlePanierDTO(1, 3)))))
        }

        @Test
        fun `add article to panier with not enough stock`() {
            // GIVEN
            val initialPanier = Panier("email", mutableListOf())
            val updatedPanier = Panier("email", mutableListOf(ArticlePanier(1, 3)))
            every { panierRepository.update(any()) } returns Result.success(updatedPanier)
            every { panierRepository.get(any()) } returns initialPanier
            every { httpService.get(any()) } returns "{\n" +
                    "    \"id\": 1,\n" +
                    "    \"nom\": \"string\",\n" +
                    "    \"prix\": 0,\n" +
                    "    \"qteStock\": 2,\n" +
                    "    \"dateMAJ\": \"2023-12-13\"\n" +
                    "}"
            // WHEN
            val result = panierController.addQuantityArticlePanier("email", 1, 3)
            // THEN
            assertThat(result).isEqualTo(ResponseEntity.badRequest().body("La quantité demandée est supérieure à la quantité disponible"))
        }
    }

}


















