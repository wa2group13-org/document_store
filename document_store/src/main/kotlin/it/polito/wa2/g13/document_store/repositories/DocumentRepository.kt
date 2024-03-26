package it.polito.wa2.g13.document_store.repositories

import it.polito.wa2.g13.document_store.data.DocumentMetadata
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : PagingAndSortingRepository<DocumentMetadata, Long> {
    fun getDocumentMetadataById(id: Long): DocumentMetadata?
}