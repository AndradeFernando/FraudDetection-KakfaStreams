package com.pocs.infrastructure

import com.pocs.domain.FinancialTransaction
import com.pocs.domain.FraudulentTransaction
import com.pocs.domain.FraudulentTransactionStreamProcessing
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class FraudulentTransactionStreamProcessingImp: FraudulentTransactionStreamProcessing {

    @Bean
    fun filteringPossibleFraud(streamsBuilder: StreamsBuilder): KStream<String, FinancialTransaction> {
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