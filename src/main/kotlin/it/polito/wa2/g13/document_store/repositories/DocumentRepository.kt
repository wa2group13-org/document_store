package it.polito.wa2.g13.document_store.repositories

import it.polito.wa2.g13.document_store.data.DocumentMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : JpaRepository<DocumentMetadata, Long> {

    fun getDocumentMetadataById(id: Long): DocumentMetadata?

    fun findAllByMailId(mailId: String): List<DocumentMetadata>
}