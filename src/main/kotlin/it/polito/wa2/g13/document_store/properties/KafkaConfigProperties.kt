package it.polito.wa2.g13.document_store.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

/**
 * These properties are used to configure the topics to receive
 * information on the kafka replicas.
 */
@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigProperties(
    /**
     * Topic of the attachments in Kafka
     */
    @param:DefaultValue("attachment.json")
    var attachmentTopic: String = "attachment.json"
)
