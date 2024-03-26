package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.services.DocumentService
import it.polito.wa2.g13.document_store.util.Err
import it.polito.wa2.g13.document_store.util.Ok
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
    fun getDocumentDetails(
        @PathVariable("metadataId") metadataId: Long
    ): DocumentMetadataDTO? {
        return documentService.getDocumentMetadataById(metadataId)
    }

    @GetMapping("{metadataId}/data")
    fun getDocumentBytes(
        @PathVariable("metadataId") metadataId: Long
    ): String {
        return documentService.getDocumentBytes(metadataId)
    }

    @PostMapping("")
    fun addDocument(@RequestParam("file") file: MultipartFile): ResponseEntity<Unit> {
        val document = UserDocumentDTO.from(file).let {
            when (it) {
                is Ok -> it.t
                is Err -> return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, it.err))
                    .build()
            }
        }

        documentService.saveDocument(document)

        return ResponseEntity.ok().build()
    }

    @PutMapping("{metadataId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateDocumentDetails(
        @PathVariable("metadataId") metadataId: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Unit> {
        val document = UserDocumentDTO.from(file).let {
            when (it) {
                is Ok -> it.t
                is Err -> return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, it.err))
                    .build()
            }
        }

        documentService.updateDocument(metadataId, document)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("{metadataId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDocumentDetails(@PathVariable("metadataId") metadataId: Long) {
        documentService.deleteDocument(metadataId)
    }
}