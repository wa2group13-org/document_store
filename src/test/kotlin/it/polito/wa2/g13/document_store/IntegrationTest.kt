package it.polito.wa2.g13.document_store

import org.junit.jupiter.api.BeforeAll
import org.slf4j.LoggerFactory
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.annotation.DirtiesContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@DirtiesContext
abstract class IntegrationTest {
    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16.2")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger("Postgres")))

        private val logger = LoggerFactory.getLogger(IntegrationTest::class.java)

        @BeforeAll
        @JvmStatic
        fun initDb() {
            logger.info("Initialized postgres db: ${postgres.databaseName}")
        }
    }
}