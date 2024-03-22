package it.polito.wa2.g13.document_store.data

import jakarta.persistence.*

@Entity
class DocumentFile(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val file: ByteArray,
    @OneToOne(mappedBy = "fileBytes", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val metadata: DocumentMetadata
) {

}