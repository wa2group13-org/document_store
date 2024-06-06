package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.IntegrationTest
import it.polito.wa2.g13.document_store.data.DocumentFile
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.nullable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.util.MultiValueMap
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = ["/scripts/clean_db.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("dev", "no-security")
class DocumentControllerTest : IntegrationTest() {
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
                    .filename(filename)
            }.build()
        }

        private fun createDocument(name: String = "test", bytes: String = "Default file content"): DocumentMetadata {
            return DocumentMetadata(
                0,
                UUID.randomUUID().toString(),
                name,
                0,
                "*/*",
                LocalDateTime.now().let { Date.from(it.toInstant(ZoneOffset.UTC)) },
                DocumentFile(0, bytes.toByteArray())
            )
        }
    }

    @Autowired
    private lateinit var documentRepository: DocumentRepository

    @Autowired
    private lateinit var testTemplate: TestRestTemplate

    @Test
    fun `should create a new document`() {
        val res = testTemplate.exchange<Any>(
            RequestEntity
                .post(ENDPOINT)
                .body(multipartFileRequestBodyBuilder("newName"))
        )

        assertEquals(201, res.statusCode.value())
    }

    // getAllDocuments
    //
    @Test
    fun `getAllDocuments should return all the documents`() {
        documentRepository.save(createDocument("test1"))

        val res = testTemplate.exchange<List<Any>>(
            RequestEntity
                .get("$ENDPOINT?pageNumber=0&limit=10")
                .build()
        )

        assertEquals(1, res.body?.size)
    }

    @Test
    fun `getAllDocuments should return an empty list`() {
        val res = testTemplate.exchange<List<Any>>(
            RequestEntity
                .get("$ENDPOINT?pageNumber=0&limit=10")
                .build()
        )

        assertEquals(0, res.body?.size)
    }


    // getDocument
    //
    @Test
    fun `getDocument should return the details of a document`() {
        val doc = documentRepository.save(createDocument("test1"))

        val res = testTemplate.exchange<DocumentMetadataDTO>(
            RequestEntity.get("$ENDPOINT/${doc.id}").build()
        )

        assertThat(res.body)
            .usingRecursiveComparison()
            .ignoringFields("creationTimestamp")
            .isEqualTo(DocumentMetadataDTO.from(doc))
    }

    @Test
    fun `getDocument should fail if document does not exists`() {
        val res = testTemplate.exchange<Any>(
            RequestEntity.get("$ENDPOINT/42").build()
        )

        assertEquals(404, res.statusCode.value())
    }


    @Test
    fun `it should update an existing document`() {
        val name = "super-file"
        val content = "super-file"

        val doc = documentRepository.save(createDocument("test", "test"))

        testTemplate.exchange<Any>(
            RequestEntity.put("$ENDPOINT/${doc.id}").body(multipartFileRequestBodyBuilder(name, content))
        )

        assertEquals(documentRepository.findById(doc.id).nullable()?.name, name)
        Assertions.assertArrayEquals(
            documentRepository.findById(doc.id).nullable()?.fileBytes?.file,
            content.toByteArray()
        )
    }

    @Test
    fun `it should not find the document`() {
        val res = testTemplate.exchange<Any>(
            RequestEntity.put("$ENDPOINT/69").body(multipartFileRequestBodyBuilder("test"))
        )

        assertEquals(404, res.statusCode.value())
    }

    @Test
    fun `should get a document by mailId`() {
        val document = documentRepository.save(createDocument())

        val res = testTemplate.exchange<DocumentMetadataDTO>(
            RequestEntity.get("$ENDPOINT/mailId/${document.mailId}").build()
        ).body

        assertThat(res)
            .usingRecursiveComparison()
            .ignoringFields("creationTimestamp")
            .isEqualTo(DocumentMetadataDTO.from(document))
    }

    @Test
    fun `should return 404 when mailId does not exist`() {
        val res = testTemplate.exchange<Any>(
            RequestEntity.get("$ENDPOINT/mailId/${UUID.randomUUID()}").build()
        )

        assertEquals(404, res.statusCode.value())
    }
}