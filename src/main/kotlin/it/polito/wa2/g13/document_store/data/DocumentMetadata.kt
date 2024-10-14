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
@Table(indexes = [Index(columnList = "mailId", unique = false)])
class DocumentMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    /**
     * References if this file is part of a message in the CRM service
     */
    @Column(updatable = false)
    var mailId: String?,
    @Column(unique = true)
    var name: String,
    var size: Long,
    var contentType: String,
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    var creationTimestamp: Date,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "file_id", foreignKey = ForeignKey())
    var fileBytes: DocumentFile
) {

    companion object {
        @JvmStatic
        fun from(file: UserDocumentDTO): DocumentMetadata {
            val bytes = DocumentFile(0, file.bytes.toByteArray())
            return DocumentMetadata(
                id = 0,
                mailId = file.mailId,
                name = file.name,
                size = file.size,
                contentType = file.contentType,
                creationTimestamp = Calendar.getInstance().time,
                fileBytes = bytes,
            )
        }
    }
}