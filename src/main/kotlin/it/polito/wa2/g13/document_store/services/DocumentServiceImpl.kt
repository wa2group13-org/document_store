package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.data.DocumentFile
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentFileDTO
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.repositories.DocumentFileRepository
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.exceptions.DocumentException
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val documentFileRepository: DocumentFileRepository
) : DocumentService {

    private val logger = LoggerFactory.getLogger(DocumentServiceImpl::class.java)

    override fun getDocumentByPage(pageNumber: Int, limit: Int): Page<DocumentMetadataDTO> {
        return documentRepository.findAll(PageRequest.of(pageNumber, limit)).map(DocumentMetadataDTO::from)
    }

    override fun getDocumentMetadataById(metadataId: Long): DocumentMetadataDTO {
        return documentRepository.getDocumentMetadataById(metadataId)
            ?.let { DocumentMetadataDTO.from(it) }
            ?: throw DocumentException.NotFound.from(metadataId)
    }

    override fun getDocumentBytes(metadataId: Long): DocumentFileDTO {
        return documentRepository.findByIdOrNull(metadataId)
            ?.let { documentFileRepository.findFirstByMetadataMaxVersion(it) }
            ?.let { DocumentFileDTO.from(it) }
            ?: throw DocumentException.NotFound.from(metadataId)
    }

    override fun saveDocument(documentDto: UserDocumentDTO): DocumentMetadataDTO {
        val document = DocumentMetadata.from(documentDto)

        val newDocument = documentRepository.save(document)

        logger.info("Saved ${DocumentMetadata::class.qualifiedName}@$${newDocument.id}")
        return DocumentMetadataDTO.from(newDocument)
    }

    override fun updateDocument(metadataId: Long, document: UserDocumentDTO): DocumentMetadataDTO {
        val oldDocument = documentRepository.findByIdOrNull(metadataId)
            ?: throw DocumentException.NotFound.from(metadataId)

        oldDocument.apply {
            this.name = document.name
            this.size = document.bytes?.size?.toLong() ?: oldDocument.size
            this.contentType = document.contentType
            this.jobOfferId = document.jobOfferId
            this.contactId = document.contactId
        }

        // Update the file bytes
        if (document.bytes != null) {
            val lastFile = documentFileRepository.findFirstByMetadataMaxVersion(oldDocument)
                ?: throw DocumentException.NotFound.from(metadataId)

            val newVersion = DocumentFile(0, oldDocument, lastFile.version + 1, document.bytes.toByteArray())

            oldDocument.fileBytes.add(newVersion)
        }

        val updatedDocument = documentRepository.save(oldDocument)

        logger.info("Updated Document with Id \"$metadataId\".")
        return DocumentMetadataDTO.from(updatedDocument)
    }

    override fun deleteDocument(metadataId: Long) {
        if (!documentRepository.existsById(metadataId))
            throw DocumentException.NotFound.from(metadataId)

        documentRepository.deleteById(metadataId)
        logger.info("Deleted Document with Id \"$metadataId\".")
    }

    override fun getDocumentByMailId(mailId: String): List<DocumentMetadataDTO> {
        return documentRepository.findAllByMailId(mailId).map { DocumentMetadataDTO.from(it) }
    }

    override fun getDocumentVersion(metadataId: Long, version: Long): DocumentFileDTO {
        return documentRepository.findByIdOrNull(metadataId)
            ?.let { documentFileRepository.findFirstByMetadataAndVersion(it, version) }
            ?.let { DocumentFileDTO.from(it) }
            ?: throw DocumentException.NotFound.from(metadataId)
    }

    override fun getDocumentsByContact(contactId: Long, page: Pageable): Page<DocumentMetadataDTO> {
        return documentRepository.findByContactId(contactId, page).map(DocumentMetadataDTO::from)
    }

    override fun getDocumentsByJobOffer(jobOfferId: Long, page: Pageable): Page<DocumentMetadataDTO> {
        return documentRepository.findByJobOfferId(jobOfferId, page).map(DocumentMetadataDTO::from)
    }

    override fun getDocumentLastVersion(metadataId: Long): Long {

        return documentRepository.findByIdOrNull(metadataId)
            ?.let { documentFileRepository.findFirstByMetadataMaxVersion(it) }
            ?.version
            ?: throw DocumentException.NotFound.from(metadataId)
    }
}