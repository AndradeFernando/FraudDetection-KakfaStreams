package com.pocs.webapi

import com.pocs.domain.FinancialTransaction

data class FinancialTransactionRepresentation(

    val transactionId: String,
    val accountId: String,
    val amount: Double,
    val timestamp: Long
) {
    fun toFinancialTransaction(): FinancialTransaction {
        return FinancialTransaction(transactionId, accountId, amount, timestamp)
    }
}
