package it.polito.wa2.g13.document_store.util.exceptions

sealed class DocumentException(override val message: String?) : RuntimeException(message)

data class DocumentDuplicateException(override val message: String?) : DocumentException(message)
data class DocumentNotFoundException(override val message: String?) : DocumentException(message)