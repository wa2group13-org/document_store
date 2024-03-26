package it.polito.wa2.g13.document_store.data

import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import jakarta.persistence.*
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
    @Column(unique = true)
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
        fun from(file: UserDocumentDTO): DocumentMetadata {
            val bytes = DocumentFile(0, file.bytes.toByteArray())
            return DocumentMetadata(
                0,
                file.name,
                file.size,
                file.contentType,
                Calendar.getInstance().time,
                bytes
            )
        }
    }
}