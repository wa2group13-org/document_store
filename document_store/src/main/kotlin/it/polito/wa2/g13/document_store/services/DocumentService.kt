package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO

interface DocumentService {
    fun getDocumentByPage(pageNumber: Int, limit: Int): List<DocumentMetadataDTO>
    fun getDocumentMetadataById(metadataId: Long): DocumentMetadataDTO?
}