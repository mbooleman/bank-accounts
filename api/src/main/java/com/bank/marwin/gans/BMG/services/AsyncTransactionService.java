package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.controllers.UserController;
import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.models.TransactionStatus;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AsyncTransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @NewSpan
    @Scheduled(fixedDelay = 10000)
    public void runTransactionExecutor() {
        LOGGER.info("start processing run for transactions");
        if (transactionRepository.countTransactionsForStatus(TransactionStatus.FAILED) > 0) {
            Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(TransactionStatus.FAILED,
                    PageRequest.of(0, 10));

            LOGGER.info("found {} failed transactions, start executing", transactions.stream().count());

            executeTransactions(transactions);
        } else if (transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING) > 0) {
            Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(TransactionStatus.PENDING,
                    PageRequest.of(0, 10));

            LOGGER.info("found {} pending transactions, start executing", transactions.stream().count());

            executeTransactions(transactions);
        }
    }

    @NewSpan
    @Transactional(rollbackOn = Exception.class)
    private void executeTransactions(Page<Transaction> transactions) {
        transactions.forEach(transaction -> {
            bankAccountService.processTransaction(transaction);
            transactionRepository.completeTransaction(transaction.getId());
            LOGGER.info("transaction with id {} completed processing", transaction.getId());
        });
    }
}
