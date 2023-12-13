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
import spring.kotlin.controller.dto.UserDTO
import spring.kotlin.domain.User
import spring.kotlin.repository.UserRepository

@SpringBootTest
class UserControllerTest {
    @MockkBean
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userController: UserController
}