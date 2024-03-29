package spring.kotlin;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserApp

fun main(args: Array<String>) {
    runApplication<UserApp>(*args)
}
