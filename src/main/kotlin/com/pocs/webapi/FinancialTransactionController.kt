package com.pocs.webapi

import com.pocs.domain.FinancialTransactionProducerService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/financial-transaction")
class FinancialTransactionController(private val financialTransactionProducerService: FinancialTransactionProducerService) {


    @PostMapping
    fun produceFinancialTransaction(@RequestBody financialTransactionRepresentation: FinancialTransactionRepresentation){
        financialTransactionProducerService.sendMessage(financialTransactionRepresentation.toFinancialTransaction())
    }

}