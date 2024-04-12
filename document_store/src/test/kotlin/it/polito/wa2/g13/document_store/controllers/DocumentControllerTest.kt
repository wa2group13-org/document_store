package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.data.DocumentFile
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.nullable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DocumentControllerTest {
    companion object {
        @Suppress("unused")
        private val logger = LoggerFactory.getLogger(DocumentControllerTest::class.java)

        private const val ENDPOINT = "/API/documents"

        private fun multipartFileRequestBodyBuilder(
            filename: String,
            content: String = "Default file content"
        ): MultiValueMap<String, HttpEntity<*>> {
            return MultipartBodyBuilder().apply {
                part("file", content.toByteArray())
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(
                        "Content-Disposition",
                        "form-data; name=file; filename=${filename}"
                    )
            }.build()
        }

        private fun createDocument(name: String = "test", bytes: String = "Default file content"): DocumentMetadata {
            return DocumentMetadata(
                0,
                name,
                0,
                "*/*",
                Calendar.getInstance().time,
                DocumentFile(0, bytes.toByteArray())
            )
        }
    }

    @Autowired
    private lateinit var documentRepository: DocumentRepository

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `should create a new document`() {
        webClient.post()
            .uri(ENDPOINT)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(multipartFileRequestBodyBuilder("newName"))
            .exchange()
            .expectStatus().isCreated()
    }

    // getAllDocuments
    //
    @Test
    fun `getAllDocuments should return all the documents`() {
        documentRepository.save(createDocument("test1"))

        webClient.get()
            .uri("$ENDPOINT?pageNumber=0&limit=10")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(DocumentMetadataDTO::class.java).hasSize(1)
    }

    @Test
    fun `getAllDocuments should return an empty list`() {
        webClient.get()
            .uri("$ENDPOINT?pageNumber=0&limit=10")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(DocumentMetadataDTO::class.java).hasSize(0)
    }


    // getDocument
    //
    @Test
    fun `getDocument should return the details of a document`() {
        val doc = documentRepository.save(createDocument("test1"))

        webClient.get()
            .uri("$ENDPOINT/${doc.id}")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(DocumentMetadataDTO::class.java).isEqualTo(DocumentMetadataDTO.from(doc))
    }

    @Test
    fun `getDocument should fail if document does not exists`() {
        webClient.get()
            .uri("$ENDPOINT/42")
            .exchange()
            .expectStatus().isNotFound()
    }


    @Test
    fun `it should update an existing document`() {
        val name = "super-file"
        val content = "super-file"

        val doc = documentRepository.save(createDocument("test", "test"))

        webClient.put()
            .uri("$ENDPOINT/${doc.id}")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(multipartFileRequestBodyBuilder(name, content))
            .exchange()
            .expectStatus().is2xxSuccessful()

        Assertions.assertEquals(documentRepository.findById(doc.id).nullable()?.name, name)
        Assertions.assertArrayEquals(
            documentRepository.findById(doc.id).nullable()?.fileBytes?.file,
            content.toByteArray()
        )
    }

    @Test
    fun `it should not find the document`() {
        webClient.put()
            .uri("$ENDPOINT/69")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(multipartFileRequestBodyBuilder("test"))
            .exchange()
            .expectStatus().isNotFound()
    }
}