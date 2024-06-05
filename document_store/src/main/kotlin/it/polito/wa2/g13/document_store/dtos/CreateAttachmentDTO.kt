package it.polito.wa2.g13.document_store.dtos

/**
 * Message coming form the kafka topic. Allows to get the mail attachments
 * from the `communication_manager`.
 */
data class CreateAttachmentDTO(
    val bytes: List<Byte>,
    val contentType: String,
    val filename: String,
    val attachmentId: String,
    val mailId: String,
)
