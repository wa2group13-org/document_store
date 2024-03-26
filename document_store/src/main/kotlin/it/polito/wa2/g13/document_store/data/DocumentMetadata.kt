package it.polito.wa2.g13.document_store.data

import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import it.polito.wa2.g13.document_store.util.Result
import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * All [Entity] classes **MUST** be plain classes and not data classes,
 * also all their properties **MUST** be `var` and not `val`.
 */
@Suppress("unused")
@Entity
class DocumentMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    var size: Long,
    var contentType: String,
    @Temporal(TemporalType.TIMESTAMP)
    var creationTimestamp: Date,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "file_id")
    var fileBytes: DocumentFile
) {

    companion object {
        @JvmStatic
        fun from(file: MultipartFile): Result<DocumentMetadata, String> {
            val bytes = DocumentFile(0, file.bytes)
            val metadata =
                DocumentMetadata(
                    0,
                    file.originalFilename ?: return Err("Missing originalFilename"),
                    file.size,
                    file.contentType ?: return Err("Missing contentType"),
                    Calendar.getInstance().time,
                    bytes
                )

            return Ok(metadata)
        }
    }
}