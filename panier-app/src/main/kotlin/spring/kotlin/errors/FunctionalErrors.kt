package spring.kotlin.errors

sealed class FunctionalErrors(message: String = "", cause: Exception? = null) :
        Exception(message, cause)

class PanierNotFoundError(email: String) : FunctionalErrors(message = "User $email not found")

