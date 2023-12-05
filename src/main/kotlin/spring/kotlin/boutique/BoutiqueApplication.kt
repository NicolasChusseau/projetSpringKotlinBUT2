package spring.kotlin.boutique

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoutiqueApplication

fun main(args: Array<String>) {
	runApplication<BoutiqueApplication>(*args)
}
