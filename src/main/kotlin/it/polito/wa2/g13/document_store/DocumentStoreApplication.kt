package it.polito.wa2.g13.document_store

import it.polito.wa2.g13.document_store.properties.KafkaConfigProperties
import it.polito.wa2.g13.document_store.properties.OpenapiConfigProperties
import it.polito.wa2.g13.document_store.properties.ProjectConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    KafkaConfigProperties::class,
    OpenapiConfigProperties::class,
    ProjectConfigProperties::class,
)
class DocumentStoreApplication

fun main(args: Array<String>) {
    runApplication<DocumentStoreApplication>(*args)
}
