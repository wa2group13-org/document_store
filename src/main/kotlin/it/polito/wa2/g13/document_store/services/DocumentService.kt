package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.dtos.DocumentFileDTO
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import org.springframework.data.domain.Page

interface DocumentService {
    fun getDocumentByPage(pageNumber: Int, limit: Int): Page<DocumentMetadataDTO>

    fun getDocumentMetadataById(metadataId: Long): DocumentMetadataDTO

    fun getDocumentBytes(metadataId: Long): DocumentFileDTO

    /**
     * Saves a [UserDocumentDTO] and return the new ID associated with it.
     */
    fun saveDocument(documentDto: UserDocumentDTO): DocumentMetadataDTO

    fun updateDocument(metadataId: Long, document: UserDocumentDTO): DocumentMetadataDTO

    /**
     * Deletes a [it.polito.wa2.g13.document_store.data.DocumentMetadata] and a [it.polito.wa2.g13.document_store.data.DocumentFile]
     * from the DB.
     */
    fun deleteDocument(metadataId: Long)

    /**
     * Get a [it.polito.wa2.g13.document_store.data.DocumentMetadata] by the [mailId] field
     */
    fun getDocumentByMailId(mailId: String): List<DocumentMetadataDTO>

    fun getDocumentVersion(metadataId: Long, version: Long): DocumentFileDTO
}