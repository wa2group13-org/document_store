package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.exceptions.DocumentDuplicateException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class DocumentServiceImpl(private val documentRepository: DocumentRepository) : DocumentService {

    private val logger = LoggerFactory.getLogger(DocumentServiceImpl::class.java)

    override fun getDocumentByPage(pageNumber: Int, limit: Int): List<DocumentMetadataDTO> {
        return documentRepository.findAll(PageRequest.of(pageNumber, limit)).map(DocumentMetadataDTO::from).toList()
    }

    override fun getDocumentMetadataById(metadataId: Long): DocumentMetadataDTO? {
        return documentRepository.getDocumentMetadataById(metadataId)?.let { DocumentMetadataDTO.from(it) }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun saveDocument(document: UserDocumentDTO) {
        try {
            documentRepository.save(DocumentMetadata.from(document))
        } catch (e: DataIntegrityViolationException) {
            throw DocumentDuplicateException(e.message)
        }
    }
}