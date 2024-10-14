package it.polito.wa2.g13.document_store.aspects

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component


sealed class DocumentError {
    data class NotFound(val message: String) : DocumentError()
    data class Duplicate(val message: String) : DocumentError()
}

@Target(AnnotationTarget.FUNCTION)
annotation class DocumentResult

@Aspect
@Component
class DocumentAspect {
    @Around("@annotation(DocumentResult)")
    fun handleDocumentErrors(joint: ProceedingJoinPoint): ResponseEntity<Any?> {
        val result = joint.proceed()

        return handleResult<DocumentError>(result) { error ->
            when (error) {
                is DocumentError.NotFound -> ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, error.message)
                is DocumentError.Duplicate -> ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, error.message)
            }
        }
    }
}