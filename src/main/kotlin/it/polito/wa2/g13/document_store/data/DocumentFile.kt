package it.polito.wa2.g13.document_store.data

import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["metadata_id", "version"])])
class DocumentFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "metadata_id", foreignKey = ForeignKey())
    var metadata: DocumentMetadata,
    var version: Long,
    var file: ByteArray,
)