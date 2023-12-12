package spring.kotlin.errors

sealed class FunctionalErrors(message: String = "", cause: Exception? = null) :
        Exception(message, cause)

class PanierNotFoundError(id: String) : FunctionalErrors(message = "Panier $id not found")

