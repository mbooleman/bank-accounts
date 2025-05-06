package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.TransactionDto;
import com.bank.marwin.gans.BMG.controllers.dtos.TransactionResponseDto;
import com.bank.marwin.gans.BMG.models.PreProcessingTransaction;
import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("")
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        PreProcessingTransaction preProcessingTransaction = transactionDto.toDomain();

        Transaction transaction = transactionService.createTransaction(preProcessingTransaction);

        return ResponseEntity.ok(new TransactionResponseDto(transaction));
    }
}
