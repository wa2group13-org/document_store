package it.polito.wa2.g13.document_store.kafka

import it.polito.wa2.g13.document_store.dtos.CreateAttachmentDTO
import it.polito.wa2.g13.document_store.dtos.UserDocumentDTO
import it.polito.wa2.g13.document_store.services.DocumentService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class KafkaDocumentConsumer(
    private val documentService: DocumentService,
) {
    @KafkaListener(id = "document-store-documents", topics = ["\${kafka-config.attachment-topic}"])
    fun getDocument(@Payload attachment: CreateAttachmentDTO) {
        val document = UserDocumentDTO.from(attachment)
        documentService.saveDocument(document)
    }
}