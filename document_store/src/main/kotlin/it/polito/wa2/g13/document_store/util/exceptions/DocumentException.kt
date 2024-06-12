package it.polito.wa2.g13.document_store.util.exceptions

import it.polito.wa2.g13.document_store.data.DocumentMetadata

sealed class DocumentException(override val message: String, override val cause: Throwable?) :
    RuntimeException(message, cause) {

    data class Duplicate(override val message: String, override val cause: Throwable? = null) :
        DocumentException(message, cause) {
        companion object {
            @JvmStatic
            fun from(name: String): Duplicate = Duplicate(
                message = "${DocumentMetadata::class.qualifiedName} with name \"$name\" already exists"
            )
        }
    }

    data class NotFound(override val message: String, override val cause: Throwable? = null) :
        DocumentException(message, cause) {
        companion object {
            @JvmStatic
            fun from(id: Long): NotFound = NotFound(
                message = "${DocumentMetadata::class.qualifiedName}@${id} does not exists"
            )
        }
    }

    data class Validation(override val message: String, override val cause: Throwable? = null) :
        DocumentException(message, cause) {
        companion object {
            @JvmStatic
            fun from(cause: String): Validation = Validation(
                message = "Could not validate: $cause"
            )
        }
    }
}
