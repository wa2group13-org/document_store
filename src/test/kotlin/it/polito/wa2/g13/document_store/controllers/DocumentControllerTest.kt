package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.IntegrationTest
import it.polito.wa2.g13.document_store.data.DocumentFile
import it.polito.wa2.g13.document_store.data.DocumentMetadata
import it.polito.wa2.g13.document_store.dtos.DocumentFileDTO
import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import it.polito.wa2.g13.document_store.repositories.DocumentFileRepository
import it.polito.wa2.g13.document_store.repositories.DocumentRepository
import it.polito.wa2.g13.document_store.util.ResultPage
import it.polito.wa2.g13.document_store.util.nullable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = ["/scripts/clean_db.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("dev", "no-security")
class DocumentControllerTest : IntegrationTest() {
    companion object {
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
                ZonedDateTime.now(),
                mutableSetOf(),
            ).apply {
                fileBytes.add(DocumentFile(0, this, 1, bytes.toByteArray()))
            }
        }
    }

    @Autowired
    private lateinit var documentRepository: DocumentRepository

    @Autowired
    private lateinit var documentFileRepository: DocumentFileRepository

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

        val res = testTemplate.exchange<ResultPage<Any>>(
            RequestEntity
                .get("$ENDPOINT?pageNumber=0&limit=10")
                .build()
        )

        assertEquals(1, res.body?.numberOfElements)
    }

    @Test
    fun `getAllDocuments should return an empty list`() {
        val res = testTemplate.exchange<ResultPage<Any>>(
            RequestEntity
                .get("$ENDPOINT?pageNumber=0&limit=10")
                .build()
        )

        assertEquals(0, res.body?.numberOfElements)
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

        val r = testTemplate.exchange<DocumentMetadataDTO>(
            RequestEntity.post(ENDPOINT).body(multipartFileRequestBodyBuilder("test", "test"))
        )

        val doc = r.body!!

        val res = testTemplate.exchange<Any>(
            RequestEntity.put("$ENDPOINT/${doc.id}").body(multipartFileRequestBodyBuilder(name, content))
        )

        assertEquals(true, res.statusCode.is2xxSuccessful)

        val res3 = testTemplate.exchange<DocumentFileDTO>(
            RequestEntity.get("$ENDPOINT/${doc.id}/data").build()
        )

        assertEquals(documentRepository.findById(doc.id).nullable()?.name, name)
        Assertions.assertArrayEquals(
            content.toByteArray(),
            res3.body?.bytes
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
    fun `should get a documents by mailId`() {
        val document = documentRepository.save(createDocument())

        val res = testTemplate.exchange<List<DocumentMetadataDTO>>(
            RequestEntity.get("$ENDPOINT/mailId/${document.mailId}").build()
        ).body

        assertThat(res)
            .usingRecursiveComparison()
            .ignoringFields("creationTimestamp")
            .isEqualTo(listOf(DocumentMetadataDTO.from(document)))
    }

    @Test
    fun `should return empty list when mailId does not exist`() {
        val res = testTemplate.exchange<List<DocumentMetadataDTO>>(
            RequestEntity.get("$ENDPOINT/mailId/${UUID.randomUUID()}").build()
        )

        assertEquals(listOf<DocumentMetadataDTO>(), res.body)
    }
}