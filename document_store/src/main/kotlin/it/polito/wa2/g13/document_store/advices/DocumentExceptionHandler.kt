package it.polito.wa2.g13.document_store.advices

import it.polito.wa2.g13.document_store.util.exceptions.DocumentDuplicateException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class DocumentExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(DocumentDuplicateException::class)
    fun handleDocumentDuplicate(e: DocumentDuplicateException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)
    }
}