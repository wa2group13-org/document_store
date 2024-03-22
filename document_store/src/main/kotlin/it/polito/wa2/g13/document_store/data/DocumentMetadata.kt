package it.polito.wa2.g13.document_store.data

import jakarta.persistence.*

@Entity
class DocumentMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val name: String,
    val size: Long,
    val contentType: String,
    @Temporal(TemporalType.TIMESTAMP)
    val creationTimestamp: java.util.Date,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "file_id")
    val fileBytes: DocumentFile
) {


}