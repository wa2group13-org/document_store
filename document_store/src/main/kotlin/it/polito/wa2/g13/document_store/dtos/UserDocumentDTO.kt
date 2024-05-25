package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import it.polito.wa2.g13.document_store.util.Result
import org.springframework.web.multipart.MultipartFile

data class UserDocumentDTO(
    val messageId: Long?,
    val name: String,
    val contentType: String,
    val size: Long,
    val bytes: List<Byte>,
) {
    companion object {
        @JvmStatic
        fun from(file: MultipartFile, messageId: Long?): Result<UserDocumentDTO, String> {
            if (file.originalFilename?.length == 0)
                return Err("Missing `filename`.")

            return Ok(
                UserDocumentDTO(
                    messageId,
                    file.originalFilename ?: return Err("Missing `filename`."),
                    file.contentType ?: return Err("Missing `contentType`."),
                    file.size,
                    file.bytes.toList(),
                )
            )
        }
    }
}