package com.pocs.webapi

import com.pocs.domain.FinancialTransactionProducerService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random
import kotlin.random.nextInt

@Validated
@RestController
@RequestMapping("/financial-transaction")
class FinancialTransactionController(private val financialTransactionProducerService: FinancialTransactionProducerService) {

    @PostMapping
    fun produceFinancialTransactions(@RequestBody financialTransactionRepresentation: FinancialTransactionRepresentation){

        for(i in 1..100000) {
            val ft =
                financialTransactionRepresentation
                .copy(transactionId = i.toString(),
                    accountId = "acc-id-$i",
                    amount = Random.nextDouble(1.0, 1000000.0),
                timestamp = Random.nextLong(111111111L,9999999999L)
                )
            financialTransactionProducerService.sendMessage(ft.toFinancialTransaction())
        }
    }

}