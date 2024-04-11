package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.aspects.DocumentResult
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.services.DocumentService
import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/API/documents")
class DocumentController(
    private val documentService: DocumentService,
) {

//    private val logger = LoggerFactory.getLogger(DocumentController::class.java)

    @GetMapping("")
    fun getAllDocuments(
        @RequestParam("pageNumber") pageNumber: Int,
        @RequestParam("limit") limit: Int,
    ): List<DocumentMetadataDTO> {
        return documentService.getDocumentByPage(pageNumber, limit)
    }

    @GetMapping("{metadataId}")
    @DocumentResult
    fun getDocument(
        @PathVariable("metadataId") metadataId: Long
    ): Any {
        return documentService.getDocumentMetadataById(metadataId).map {
            ResponseEntity.ok(it)
        }
    }

    @GetMapping("{metadataId}/data")
    @DocumentResult
    fun getDocumentBytes(
        @PathVariable("metadataId") metadataId: Long
    ): Any {
        return documentService.getDocumentBytes(metadataId).map {
            ResponseEntity.ok(it)
        }
    }

    @PostMapping("")
    @DocumentResult
    fun addDocument(
        request: HttpServletRequest,
        @RequestParam("file") file: MultipartFile
    ): Any {
        val document = UserDocumentDTO.from(file).let {
            when (it) {
                is Ok -> it.t
                is Err -> return Ok<ResponseEntity<ProblemDetail>, ProblemDetail>(
                    ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, it.err)).build()
                )
            }
        }

        return documentService.saveDocument(document).map {
            ResponseEntity.created(URI.create("${request.requestURI}/$it")).build<Unit>()
        }
    }

    @PutMapping("{metadataId}")
    @DocumentResult
    fun updateDocument(
        @PathVariable("metadataId") metadataId: Long,
        @RequestParam("file") file: MultipartFile
    ): Any {
        val document = UserDocumentDTO.from(file).let {
            when (it) {
                is Ok -> it.t
                is Err -> return Ok<ResponseEntity<*>, Any>(
                    ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, it.err))
                        .build<Any>()
                )
            }
        }

        return documentService.updateDocument(metadataId, document).map {
            ResponseEntity.noContent().build<Any>()
        }
    }

    @DeleteMapping("{metadataId}")
    @DocumentResult
    fun deleteDocument(@PathVariable("metadataId") metadataId: Long): Any {
        return documentService.deleteDocument(metadataId).map {
            ResponseEntity.ok().build<Unit>()
        }
    }
}