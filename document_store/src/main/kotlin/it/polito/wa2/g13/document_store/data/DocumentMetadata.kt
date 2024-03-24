package it.polito.wa2.g13.document_store.data

import jakarta.persistence.*
import java.util.*

@Entity
data class DocumentMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val size: Long,
    val contentType: String,
    @Temporal(TemporalType.TIMESTAMP)
    val creationTimestamp: Date,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "file_id")
    val fileBytes: DocumentFile
)