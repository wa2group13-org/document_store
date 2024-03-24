package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.data.DocumentMetadata

// TODO: [Brendon] decide the fields to exchange
data class DocumentDTO(
    val id: Long,
    val name: String,
    val contentType: String,
    val bytes: String?,
) {

    companion object {
        @JvmStatic
        fun from(d: DocumentMetadata): DocumentDTO = DocumentDTO(
            d.id,
            d.name,
            d.contentType,
            null,
        )
    }
}
