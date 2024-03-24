package it.polito.wa2.g13.document_store.repositories

import it.polito.wa2.g13.document_store.data.DocumentMetadata
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : CrudRepository<DocumentMetadata, Long> {
    @Query("select d from DocumentMetadata d")
    fun getDocumentsByPage(page: Pageable): List<DocumentMetadata>
}