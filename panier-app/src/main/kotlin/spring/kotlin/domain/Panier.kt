package spring.kotlin.domain


data class Panier(
        var userEmail: String,
        var articlesPanier: MutableList<ArticlePanier>
)