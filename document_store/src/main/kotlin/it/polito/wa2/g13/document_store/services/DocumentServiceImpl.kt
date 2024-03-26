package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class DocumentServiceImpl(private val documentRepository: DocumentRepository) : DocumentService {

    override fun getDocumentByPage(pageNumber: Int, limit: Int): List<DocumentMetadataDTO> {
        return documentRepository.findAll(PageRequest.of(pageNumber, limit)).map(DocumentMetadataDTO::from).toList()
    }
}