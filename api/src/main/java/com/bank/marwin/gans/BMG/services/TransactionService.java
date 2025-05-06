package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.errors.BankAccountNotFoundByIBANException;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.PreProcessingTransaction;
import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountService bankAccountService;

    public Transaction createTransaction(PreProcessingTransaction preProcessingTransaction) {
        Optional<BankAccount> fromAccount = bankAccountService.findBankAccountByIBAN(
                preProcessingTransaction.fromIban());

        if (fromAccount.isEmpty()) {
            throw new BankAccountNotFoundByIBANException(preProcessingTransaction.fromIban());
        }

        Optional<BankAccount> toAccount = bankAccountService.findBankAccountByIBAN(preProcessingTransaction.toIban());

        if (toAccount.isEmpty()) {
            throw new BankAccountNotFoundByIBANException(preProcessingTransaction.toIban());
        }

        Transaction transaction = preProcessingTransaction.toTransaction(fromAccount.get(), toAccount.get());

        return transactionRepository.save(transaction);
    }
}
