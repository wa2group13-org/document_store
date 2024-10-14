package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.data.DocumentMetadata

// TODO: [Brendon] decide the fields to exchange
data class DocumentMetadataDTO(
    val id: Long,
    val name: String,
    val size: Long,
    val contentType: String,
    val creationTimestamp: String,
) {

    companion object {
        @JvmStatic
        fun from(d: DocumentMetadata): DocumentMetadataDTO = DocumentMetadataDTO(
            d.id,
            d.name,
            d.size,
            d.contentType,
            d.creationTimestamp.toString(),
        )
    }
}
