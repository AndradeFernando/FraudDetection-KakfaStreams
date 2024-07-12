package com.pocs.infrastructure

import com.pocs.domain.FinancialTransaction
import com.pocs.domain.FinancialTransactionProducerService
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class FinancialTransactionProducerServiceImpl(private val kafkaTemplate: KafkaTemplate<String, FinancialTransaction>): FinancialTransactionProducerService {

    override fun sendMessage(transaction: FinancialTransaction) {
        kafkaTemplate.send(ProducerRecord("financial-transactions", transaction.transactionId, transaction))
    }

}