package spring.kotlin.domain

data class ArticlePanier(
        var id: Int,
        var panierId: Int,
        var articleId: Int,
        var quantite: Int
)