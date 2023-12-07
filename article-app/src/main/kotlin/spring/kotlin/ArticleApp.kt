package spring.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArticleApp

fun main(args: Array<String>) {
    runApplication<ArticleApp>(*args)
}
