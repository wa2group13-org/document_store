package it.polito.wa2.g13.document_store.repositories

import it.polito.wa2.g13.document_store.data.DocumentFile
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DocumentFileRepository : JpaRepository<DocumentFile, Int> {
    @Query("""select u
        from DocumentFile u
        where u.metadata=:metadata
        group by u.version, u.metadata, u.id
        order by max(u.version) desc
        limit 1""")
    fun findFirstByMetadataMaxVersion(metadata: DocumentMetadata): DocumentFile?

    fun findFirstByMetadataAndVersion(metadata: DocumentMetadata, version: Long): DocumentFile?
}