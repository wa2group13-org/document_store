package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.util.exceptions.DocumentException
import org.springframework.web.multipart.MultipartFile

/**
 * Represent a request to create a [it.polito.wa2.g13.document_store.data.DocumentMetadata] and a [it.polito.wa2.g13.document_store.data.DocumentFile]
 */
data class UserDocumentDTO(
    val contactId: Long?,
    val jobOfferId: Long?,
    val mailId: String?,
    val name: String,
    val contentType: String,
    val size: Long,
    val bytes: List<Byte>?,
) {
    companion object {
        @JvmStatic
        fun from(file: MultipartFile, mailId: String?): UserDocumentDTO {
            if (file.originalFilename == null || file.originalFilename!!.isEmpty())
                throw DocumentException.Validation.from("filename is empty!")

            return UserDocumentDTO(
                mailId = mailId,
                name = file.originalFilename!!,
                contentType = file.contentType ?: throw DocumentException.Validation.from("contentType is empty"),
                size = file.size,
                bytes = file.bytes.toList(),
                jobOfferId = null,
                contactId = null,
            )
        }

        @JvmStatic
        fun from(attachment: CreateAttachmentDTO) = UserDocumentDTO(
            mailId = attachment.mailId,
            name = attachment.filename,
            contentType = attachment.contentType,
            size = attachment.bytes.size.toLong(),
            bytes = attachment.bytes.toList(),
            jobOfferId = null,
            contactId = null,
        )
    }
}