package com.pocs.domain

interface FinancialTransactionProducerService {
    fun sendMessage(transaction: FinancialTransaction)
}