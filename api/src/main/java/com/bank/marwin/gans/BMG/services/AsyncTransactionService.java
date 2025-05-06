package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.models.TransactionStatus;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AsyncTransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Async("transactionExecutor")
    @Transactional(rollbackOn = Exception.class)
    public void executeTransactions(Page<Transaction> transactions) {
        transactions.forEach(transaction -> {
            bankAccountService.processTransaction(transaction);
            transactionRepository.completeTransaction(transaction.getId());
        });
    }

    @Scheduled(fixedDelay = 10000)
    public void runTransactionExecutor() {
        if (transactionRepository.countTransactionsForStatus(TransactionStatus.FAILED) > 0) {
            Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(
                    TransactionStatus.FAILED, PageRequest.of(0, 10));

            executeTransactions(transactions);
        } else if (transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING) > 0) {
            Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(
                    TransactionStatus.PENDING, PageRequest.of(0, 10));

            executeTransactions(transactions);
        }
    }
}
