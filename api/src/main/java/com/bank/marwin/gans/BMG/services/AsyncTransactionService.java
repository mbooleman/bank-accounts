package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.controllers.UserController;
import com.bank.marwin.gans.BMG.models.Transaction;
import com.bank.marwin.gans.BMG.models.TransactionStatus;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import com.bank.marwin.gans.avro.model.TransactionMessage;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AsyncTransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTransactionService.class);

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
            LOGGER.info("transaction with id {} has completed initial processing", transaction.getId());
        });
    }

    @Transactional
    @KafkaListener(topics = "transactions", groupId = "bmg-spring-transaction", containerFactory = "kafkaListenerTransactionContainerFactory")
    public void listenTransactions(TransactionMessage message) {
        System.out.println("Received Message in group bmg-spring-transaction: " + message);
        UUID id = UUID.fromString(message.getId().toString());
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new RuntimeException();
        }
        bankAccountService.transferMoneyToAccount(transaction.get().getToAccount(), transaction.get().getAmount());
        transactionRepository.completeTransaction(id);
    }

}
