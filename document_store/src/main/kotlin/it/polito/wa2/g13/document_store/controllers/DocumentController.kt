package it.polito.wa2.g13.document_store.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class DocumentController {

    @GetMapping("/API/documents")
    fun getAllDocuments(@RequestParam("pageNumber") pageNumber: Int, @RequestParam("limit") limit: Int) {
    }

    @GetMapping("/API/documents/{metadataId}")
    fun getDocumentDetails(@PathVariable("metadataId") metadataId: Int) {

    }

    @GetMapping("/API/documents/{metadataId}/data")
    fun getDocumentBytes(@PathVariable("metadataId") metadataId: Int) {

    }

    @PostMapping("API/documents")
    fun addDocument(@RequestParam("file") file: MultipartFile) {

    }

    @PutMapping("/API/documents/{metadataId}")
    fun updateDocumentDetails(@PathVariable("metadataId") metadataId: Int, @RequestParam("file") file: MultipartFile) {

    }

    @DeleteMapping("/API/documents/{metadataId}")
    fun deleteDocumentDetails(@PathVariable("metadataId") metadataId: Int) {

    }
}