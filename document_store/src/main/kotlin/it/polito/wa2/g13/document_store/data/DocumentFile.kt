package it.polito.wa2.g13.document_store.data

import jakarta.persistence.*

@Suppress("unused")
@Entity
class DocumentFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var file: ByteArray,
) {
    @OneToOne(mappedBy = "fileBytes", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    lateinit var metadata: DocumentMetadata
}