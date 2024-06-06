package it.polito.wa2.g13.document_store.services

import it.polito.wa2.g13.document_store.aspects.DocumentError
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import it.polito.wa2.g13.document_store.util.Result
import it.polito.wa2.g13.document_store.util.nullable
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class DocumentServiceImpl(private val documentRepository: DocumentRepository) : DocumentService {

    private val logger = LoggerFactory.getLogger(DocumentServiceImpl::class.java)

    override fun getDocumentByPage(pageNumber: Int, limit: Int): List<DocumentMetadataDTO> {
        return documentRepository.findAll(PageRequest.of(pageNumber, limit)).map(DocumentMetadataDTO::from).toList()
    }

    override fun getDocumentMetadataById(metadataId: Long): Result<DocumentMetadataDTO, DocumentError> {
        return documentRepository.getDocumentMetadataById(metadataId)?.let { Ok(DocumentMetadataDTO.from(it)) }
            ?: Err(DocumentError.NotFound("Document with Id\"$metadataId\" does not exists"))
    }

    override fun getDocumentBytes(metadataId: Long): Result<String, DocumentError> {
        return documentRepository.findById(metadataId).nullable()
            ?.let { Ok(Base64.getEncoder().encodeToString(it.fileBytes.file)) }
            ?: Err(DocumentError.NotFound("Document with Id \"$metadataId\" does not exists"))
    }

    override fun saveDocument(document: UserDocumentDTO): Result<Long, DocumentError> {
        val newId = try {
            val newDocument = documentRepository.save(DocumentMetadata.from(document))
            newDocument.id
        } catch (e: DataIntegrityViolationException) {
            logger.error(e.message)
            return Err(DocumentError.Duplicate(e.message!!))
        }

        logger.info("Saved ${DocumentMetadata::class.qualifiedName}@$newId")
        return Ok(newId)
    }

    override fun updateDocument(metadataId: Long, document: UserDocumentDTO): Result<Unit, DocumentError> {
        val oldDocument = documentRepository.findById(metadataId).nullable()
            ?: return Err(DocumentError.NotFound("Document with id \"$metadataId\" does not exists."))

        oldDocument.apply {
            this.name = document.name
            this.size = document.size
            this.contentType = document.contentType
            this.fileBytes.file = document.bytes.toByteArray()
        }

        documentRepository.save(oldDocument)

        logger.info("Updated Document with Id \"$metadataId\".")
        return Ok(Unit)
    }

    override fun deleteDocument(metadataId: Long): Result<Unit, DocumentError> {
        return if (documentRepository.existsById(metadataId)) {
            documentRepository.deleteById(metadataId)
            logger.info("Deleted Document with Id \"$metadataId\".")
            Ok(Unit)
        } else {
            val msg = "Document with Id \"${metadataId}\" does not exists."
            logger.error(msg)
            Err(DocumentError.NotFound(msg))
        }
    }

    override fun getDocumentByMailId(mailId: String): Result<DocumentMetadataDTO, DocumentError> {
        return documentRepository.findByMailId(mailId)
            ?.let { Ok(DocumentMetadataDTO.from(it)) }
            ?: Err(
                DocumentError.NotFound(
                    "Document with mailId@${mailId} does not exists."
                )
            )
    }
}