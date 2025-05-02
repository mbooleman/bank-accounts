package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.BankAccountResponseAccountDto;
import com.bank.marwin.gans.BMG.controllers.dtos.TransactionDto;
import com.bank.marwin.gans.BMG.errors.UserNotFoundException;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.PreProcessingTransaction;
import com.bank.marwin.gans.BMG.models.User;
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
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDto transactionDto) {
        PreProcessingTransaction preProcessingTransaction = transactionDto.toDomain();

        transactionService.createTransaction(preProcessingTransaction);

        return ResponseEntity.ok("success");
    }
}
