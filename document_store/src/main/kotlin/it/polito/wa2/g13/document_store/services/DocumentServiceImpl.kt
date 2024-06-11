package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.aspects.DocumentError
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentFileDTO
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import it.polito.wa2.g13.document_store.util.exceptions.DocumentException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DocumentServiceImpl(private val documentRepository: DocumentRepository) : DocumentService {

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
            ?.let { DocumentFileDTO.from(it.fileBytes) }
            ?: throw DocumentException.NotFound.from(metadataId)
    }

    override fun saveDocument(documentDto: UserDocumentDTO): DocumentMetadataDTO {
        val document = DocumentMetadata.from(documentDto)

        if (documentRepository.existsByName(document.name)) {
            throw DocumentException.Duplicate.from(document.name)
        }

        val newDocument = documentRepository.save(document)

        logger.info("Saved ${DocumentMetadata::class.qualifiedName}@$${newDocument.id}")
        return DocumentMetadataDTO.from(newDocument)
    }

    override fun updateDocument(metadataId: Long, document: UserDocumentDTO): DocumentMetadataDTO {
        val oldDocument = documentRepository.findByIdOrNull(metadataId)
            ?: throw DocumentException.NotFound.from(metadataId)

        oldDocument.apply {
            this.name = document.name
            this.size = document.size
            this.contentType = document.contentType
            this.fileBytes.file = document.bytes.toByteArray()
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

    override fun getDocumentByMailId(mailId: String): DocumentMetadataDTO {
        return documentRepository.findByMailId(mailId)
            ?.let { DocumentMetadataDTO.from(it) }
            ?: throw DocumentException.NotFound("${DocumentMetadata::class.qualifiedName} with mailId@${mailId} was not found")
    }
}