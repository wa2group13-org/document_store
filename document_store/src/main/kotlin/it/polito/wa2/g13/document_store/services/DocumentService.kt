package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO

interface DocumentService {
    fun getDocumentByPage(pageNumber: Int, limit: Int): List<DocumentMetadataDTO>

    fun getDocumentMetadataById(metadataId: Long): DocumentMetadataDTO

    fun getDocumentBytes(metadataId: Long): String

    fun saveDocument(document: UserDocumentDTO)

    fun updateDocument(metadataId: Long, document: UserDocumentDTO)

    fun deleteDocument(metadataId: Long)
}