package it.polito.wa2.g13.document_store.advices

import it.polito.wa2.g13.document_store.util.exceptions.DocumentException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class DocumentExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(DocumentException.Duplicate::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDocumentDuplicate(e: DocumentException.Duplicate): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message)
    }

    @ExceptionHandler(DocumentException.NotFound::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleDocumentNotFound(e: DocumentException.NotFound): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
    }
}