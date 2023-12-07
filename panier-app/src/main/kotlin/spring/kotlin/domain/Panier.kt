package spring.kotlin.domain


data class Panier(
        var id: Int,
        var userId: Int,
        var articlesPanier: List<ArticlePanier>
)