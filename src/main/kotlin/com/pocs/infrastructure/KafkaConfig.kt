package com.pocs.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.pocs.domain.FinancialTransaction
import com.pocs.domain.FraudulentTransaction
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.kafka.support.serializer.JsonSerializer
import org.w3c.dom.Entity


@Configuration
@EnableKafkaStreams
class KafkaConfig {

    @Bean
    fun defaultKafkaStreamsConfig(): KafkaStreamsConfiguration {
        val config = mapOf(
            StreamsConfig.APPLICATION_ID_CONFIG to "fraud-detection-application",
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG to Serdes.String()::class.java.name,
            StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG to JsonSerde::class.java.name,
            StreamsConfig.STATE_DIR_CONFIG to "/tmp/kafka-streams",
            JsonDeserializer.TRUSTED_PACKAGES to "com.pocs.domain"
        )

        return KafkaStreamsConfiguration(config)
    }

    @Bean
    fun createTopic(): NewTopic {
        return NewTopic("financial-transactions", 1, 1.toShort())
    }

    @Bean
    fun jsonSerde(): JsonSerde<FinancialTransaction> {
        val objectMapper = ObjectMapper()
        val jsonSerializer = JsonSerializer<FinancialTransaction>()
        val jsonDeserializer = JsonDeserializer(FinancialTransaction::class.java, objectMapper).apply {
            addTrustedPackages("com.pocs.domain")
            setRemoveTypeHeaders(false)
            setUseTypeMapperForKey(false)

        }
        return JsonSerde(jsonSerializer, jsonDeserializer)
    }



    @Bean
    fun transactionStream(streamsBuilder: StreamsBuilder): KStream<String, FinancialTransaction> {
        val stream = streamsBuilder.stream<String, FinancialTransaction>("financial-transactions")

        val fraudulentTransactions = stream.filter { _, transaction ->
            transaction.amount > 10000 // Example criteria for fraud detection
        }.mapValues { transaction ->
            FraudulentTransaction(
                transactionId = transaction.transactionId,
                accountId = transaction.accountId,
                amount = transaction.amount,
                timestamp = transaction.timestamp,
                reason = "Amount exceeds threshold"
            )
        }

        fraudulentTransactions.to("fraudulent-transactions")

        return stream
    }
}
