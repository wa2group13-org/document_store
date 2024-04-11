package it.polito.wa2.g13.document_store.controllers

import it.polito.wa2.g13.document_store.dtos.DocumentMetadataDTO
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.annotation.Rollback
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
class DocumentControllerTest {
    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        @Suppress("unused")
        val postgres = PostgreSQLContainer("postgres:16.2")
            .withDatabaseName("integration-test-db")
            .withUsername("test")
            .withPassword("test")


        @Suppress("unused")
        private val logger = LoggerFactory.getLogger(DocumentControllerTest::class.java)

        private const val ENDPOINT = "/API/documents"

        @BeforeAll
        @JvmStatic
        fun initDb(@Autowired restTemplate: TestRestTemplate) {
            val request = this.getMultipartFileRequest("test-file")

            val response = restTemplate.exchange(request, String::class.java)

            Assertions.assertTrue(response.statusCode.is2xxSuccessful)
        }

        /**
         * Get a request entity with a single [org.springframework.web.multipart.MultipartFile] body
         */
        private fun getMultipartFileRequest(
            filename: String,
            content: String = "Default file content"
        ): RequestEntity<*> {
            val body = MultipartBodyBuilder().apply {
                part("file", content.toByteArray()).header(
                    "Content-Disposition",
                    "form-data; name=file; filename=${filename}"
                )
            }.build()

            return RequestEntity
                .post(ENDPOINT)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
        }
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    @Rollback
    fun post() {
        val request = getMultipartFileRequest("super-file", "super-file")
        val response = restTemplate.exchange(request, String::class.java)

        println(request)
        println(response)
        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
    }

    @Test
    fun get() {
        val request = getMultipartFileRequest("compose.yml", "service:\n  name: boh")
        restTemplate.exchange(request, String::class.java)

        val response = restTemplate.getForEntity<List<DocumentMetadataDTO>>("$ENDPOINT?pageNumber=0&limit=10")

        Assertions.assertTrue(response.statusCode.is2xxSuccessful)
        Assertions.assertEquals(response.body?.size, 1)
    }
}