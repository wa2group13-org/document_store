package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.data.DocumentMetadata

data class DocumentMetadataDTO(
    val id: Long,
    val mailId: String?,
    val contactId: Long?,
    val jobOfferId: Long?,
    val name: String,
    val size: Long,
    val contentType: String,
    val creationTimestamp: String,
) {

    companion object {
        @JvmStatic
        fun from(d: DocumentMetadata): DocumentMetadataDTO = DocumentMetadataDTO(
            id = d.id,
            mailId = d.mailId,
            contactId = d.contactId,
            jobOfferId = d.jobOfferId,
            name = d.name,
            size = d.size,
            contentType = d.contentType,
            creationTimestamp = d.creationTimestamp.toString(),
        )
    }
}
