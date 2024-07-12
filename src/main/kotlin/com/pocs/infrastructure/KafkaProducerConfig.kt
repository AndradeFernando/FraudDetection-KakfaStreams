package com.pocs.infrastructure

import com.pocs.domain.FinancialTransaction
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {

    @Bean
    fun producerFactory(): ProducerFactory<String, FinancialTransaction> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to listOf("com.pocs.domain")
        )
        return  DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, FinancialTransaction> {
        return KafkaTemplate(producerFactory())
    }
}

fun sendMessage(kafkaTemplate: KafkaTemplate<String, FinancialTransaction>, transaction: FinancialTransaction) {
    kafkaTemplate.send(ProducerRecord("financial-transactions", transaction.transactionId, transaction))
}