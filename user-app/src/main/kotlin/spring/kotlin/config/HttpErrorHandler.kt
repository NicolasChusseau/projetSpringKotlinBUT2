package spring.kotlin.config

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import spring.kotlin.errors.CannotCreatedPanierError
import spring.kotlin.errors.UserNotFoundError

@ControllerAdvice
class HttpErrorHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatusCode, request: WebRequest): ResponseEntity<Any>? {
        return ResponseEntity.badRequest().body("You're arg is invalid")
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(e: ConstraintViolationException) =
            ResponseEntity.badRequest().body("Noooo")

    @ExceptionHandler(UserNotFoundError::class)
    fun articleNotFound(e: UserNotFoundError) = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)

    @ExceptionHandler(CannotCreatedPanierError::class)
    fun cannotCreatedPanier(e: CannotCreatedPanierError) = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)

}
