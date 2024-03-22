package it.polito.wa2.g13.document_store.repositories

import it.polito.wa2.g13.document_store.data.DocumentMetadata
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface DocumentRepository : CrudRepository<DocumentMetadata, Long> {
    @Query("select d from DocumentMetadata d")
    fun getDocuments(page: Int, limit: Int): List<DocumentMetadata>

    fun addDocument()
}