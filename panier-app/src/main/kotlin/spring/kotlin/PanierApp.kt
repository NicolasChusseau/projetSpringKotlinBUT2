package spring.kotlin;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PanierApp

fun main(args: Array<String>) {
    runApplication<PanierApp>(*args)
}
