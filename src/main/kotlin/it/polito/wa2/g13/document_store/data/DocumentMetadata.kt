package it.polito.wa2.g13.document_store.data

import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import jakarta.persistence.*
import java.time.ZonedDateTime

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
    var name: String,
    var size: Long,
    var contentType: String,
    /**
     * References a Contact (professional/customer) in the CRM.
     */
    var contactId: Long?,
    /**
     * References a JobOffer in the CRM.
     */
    var jobOfferId: Long?,
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    var creationTimestamp: ZonedDateTime,
    @OneToMany(mappedBy = "metadata", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var fileBytes: MutableSet<DocumentFile>,
) {

    companion object {
        @JvmStatic
        fun from(file: UserDocumentDTO) = DocumentMetadata(
            id = 0,
            mailId = file.mailId,
            name = file.name,
            size = file.size,
            contentType = file.contentType,
            creationTimestamp = ZonedDateTime.now(),
            fileBytes = mutableSetOf(),
            contactId = file.contactId,
            jobOfferId = file.jobOfferId,
        ).apply {
            fileBytes.add(DocumentFile(0, this, 1, file.bytes!!.toByteArray()))
        }
    }
}