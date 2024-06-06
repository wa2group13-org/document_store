package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import it.polito.wa2.g13.document_store.util.Result
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.data.DocumentFile
import org.springframework.web.multipart.MultipartFile

/**
 * Represent a request to create a [DocumentMetadata] and a [DocumentFile]
 */
data class UserDocumentDTO(
    val mailId: String?,
    val name: String,
    val contentType: String,
    val size: Long,
    val bytes: List<Byte>,
) {
    companion object {
        @JvmStatic
        fun from(file: MultipartFile, mailId: String?): Result<UserDocumentDTO, String> {
            if (file.originalFilename?.length == 0)
                return Err("Missing `filename`.")

            return Ok(
                UserDocumentDTO(
                    mailId = mailId,
                    name = file.originalFilename ?: return Err("Missing `filename`."),
                    contentType = file.contentType ?: return Err("Missing `contentType`."),
                    size = file.size,
                    bytes = file.bytes.toList(),
                )
            )
        }

        @JvmStatic
        fun from(attachment: CreateAttachmentDTO) = UserDocumentDTO(
            mailId = attachment.mailId,
            name = attachment.filename,
            contentType = attachment.contentType,
            size = attachment.bytes.size.toLong(),
            bytes = attachment.bytes.toList(),
        )
    }
}