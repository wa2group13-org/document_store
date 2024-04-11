package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.aspects.DocumentError
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.util.Result

interface DocumentService {
    fun getDocumentByPage(pageNumber: Int, limit: Int): List<DocumentMetadataDTO>

    fun getDocumentMetadataById(metadataId: Long): Result<DocumentMetadataDTO, DocumentError>

    fun getDocumentBytes(metadataId: Long): Result<String, DocumentError>

    /**
     * Saves a [UserDocumentDTO] and return the new ID associated with it.
     */
    fun saveDocument(document: UserDocumentDTO): Result<Long, DocumentError>

    fun updateDocument(metadataId: Long, document: UserDocumentDTO): Result<Unit, DocumentError>

    /**
     * Deletes a [it.polito.wa2.g13.document_store.data.DocumentMetadata] and a [it.polito.wa2.g13.document_store.data.DocumentFile]
     * from the DB.
     */
    fun deleteDocument(metadataId: Long): Result<Unit, DocumentError>
}