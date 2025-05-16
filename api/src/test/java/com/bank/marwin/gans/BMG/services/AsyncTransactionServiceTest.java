package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.*;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AsyncTransactionServiceTest {
    @InjectMocks
    private AsyncTransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountService accountService;

    @Test
    void runTransactionExecutorWorksForPendingTransaction() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount toAccount = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L,
                user, Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount fromAccount = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2",
                200L, user, Currency.getInstance("EUR"));

        Transaction transaction = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "test description", Instant.now(), TransactionStatus.PENDING);

        Mockito.when(transactionRepository.countTransactionsForStatus(TransactionStatus.FAILED)).thenReturn(0);
        Mockito.when(transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING)).thenReturn(1);
        Mockito.when(transactionRepository.getTransactionsForStatus(TransactionStatus.PENDING, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        transactionService.runTransactionExecutor();

        verify(accountService, times(1)).processTransaction(transaction);
        verify(transactionRepository, times(0)).getTransactionsForStatus(TransactionStatus.FAILED,
                PageRequest.of(0, 10));
    }

    @Test
    void runTransactionExecutorWorksForFailedTransaction() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount toAccount = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L,
                user, Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount fromAccount = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2",
                200L, user, Currency.getInstance("EUR"));

        Transaction transaction = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "test description", Instant.now(), TransactionStatus.FAILED);

        Mockito.when(transactionRepository.countTransactionsForStatus(TransactionStatus.FAILED)).thenReturn(1);
        Mockito.when(transactionRepository.getTransactionsForStatus(TransactionStatus.FAILED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        transactionService.runTransactionExecutor();

        verify(accountService, times(1)).processTransaction(transaction);
        verify(transactionRepository, times(0)).getTransactionsForStatus(TransactionStatus.PENDING,
                PageRequest.of(0, 10));
    }

    @Test
    void runTransactionExecutorWorksForNoTransactions() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount toAccount = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L,
                user, Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount fromAccount = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2",
                200L, user, Currency.getInstance("EUR"));

        Transaction transaction = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "test description", Instant.now(), TransactionStatus.FAILED);

        Mockito.when(transactionRepository.countTransactionsForStatus(TransactionStatus.FAILED)).thenReturn(0);
        Mockito.when(transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING)).thenReturn(0);

        transactionService.runTransactionExecutor();

        verify(accountService, times(0)).processTransaction(transaction);
        verify(transactionRepository, times(0)).completeTransaction(transaction.getId());
        verify(transactionRepository, times(0)).getTransactionsForStatus(TransactionStatus.PENDING,
                PageRequest.of(0, 10));
        verify(transactionRepository, times(0)).getTransactionsForStatus(TransactionStatus.FAILED,
                PageRequest.of(0, 10));
    }
}