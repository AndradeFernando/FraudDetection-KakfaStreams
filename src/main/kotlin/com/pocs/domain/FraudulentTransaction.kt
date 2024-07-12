package com.pocs.domain

data class FraudulentTransaction(
    val transactionId: String,
    val accountId: String,
    val amount: Double,
    val timestamp: Long,
    val reason: String
)