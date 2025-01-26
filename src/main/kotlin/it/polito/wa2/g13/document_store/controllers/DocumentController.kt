package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.dtos.DocumentFileDTO
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.services.DocumentService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/API/documents")
class DocumentController(
    private val documentService: DocumentService,
) {
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getAllDocuments(
        @RequestParam("pageNumber") pageNumber: Int,
        @RequestParam("limit") limit: Int,
    ): Page<DocumentMetadataDTO> {
        return documentService.getDocumentByPage(pageNumber, limit)
    }

    @GetMapping("{metadataId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDocument(
        @PathVariable("metadataId") metadataId: Long
    ): DocumentMetadataDTO {
        return documentService.getDocumentMetadataById(metadataId)
    }

    @GetMapping("{metadataId}/data")
    @ResponseStatus(HttpStatus.OK)
    fun getDocumentBytes(
        @PathVariable("metadataId") metadataId: Long
    ): DocumentFileDTO {
        return documentService.getDocumentBytes(metadataId)
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun addDocument(
        request: HttpServletRequest,
        @RequestPart("file") file: MultipartFile,
        @RequestPart("mailId") mailId: String?,
    ): ResponseEntity<DocumentMetadataDTO> {
        val document = UserDocumentDTO.from(file, mailId)

        val documentDto = documentService.saveDocument(document)

        return ResponseEntity.created(URI.create("${request.requestURI}/${documentDto.id}")).body(documentDto)
    }

    @PutMapping("{metadataId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateDocument(
        @PathVariable("metadataId") metadataId: Long,
        @RequestPart("file") file: MultipartFile,
        @RequestPart("mailId") mailId: String?,
    ): DocumentMetadataDTO {
        val documentDto = UserDocumentDTO.from(file, mailId)

        return documentService.updateDocument(metadataId, documentDto)
    }

    @DeleteMapping("{metadataId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteDocument(@PathVariable("metadataId") metadataId: Long) {
        return documentService.deleteDocument(metadataId)
    }

    @GetMapping("mailId/{mailId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDocumentByMailId(@PathVariable("mailId") mailId: String): List<DocumentMetadataDTO> {
        return documentService.getDocumentByMailId(mailId)
    }

    @GetMapping("{metadataId}/versions/{version}")
    @ResponseStatus(HttpStatus.OK)
    fun getDocumentByIdAndVersion(
        @PathVariable metadataId: Long,
        @PathVariable version: Long,
    ): DocumentFileDTO {
        return documentService.getDocumentVersion(metadataId, version)
    }
}