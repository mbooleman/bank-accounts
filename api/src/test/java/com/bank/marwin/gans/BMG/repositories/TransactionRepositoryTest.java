package com.bank.marwin.gans.BMG.repositories;

import com.bank.marwin.gans.BMG.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class TransactionRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            "postgres:17.4-alpine").withDatabaseName("integration-tests-db").withUsername("sa").withPassword("sa");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearTables() {
        transactionRepository.deleteAll();
        bankAccountRepository.deleteAll();
        userRepository.deleteAll();
    }


    private final UUID userId = UUID.randomUUID();
    private final User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));


    private final IBAN fromAccountIBAN = new IBAN("NL12ABCD0123456789");

    private final UUID fromAccountId = UUID.randomUUID();
    private final BankAccount fromAccount = new BankAccount(fromAccountId, fromAccountIBAN, AccountType.SAVINGS_ACCOUNT,
            "mijn account", 1234L, user, Currency.getInstance("EUR"));

    private final IBAN toAccountIBAN = new IBAN("NL12ABCD0987654321");

    private final UUID toAccountId = UUID.randomUUID();
    private final BankAccount toAccount = new BankAccount(toAccountId, toAccountIBAN, AccountType.CHECKING_ACCOUNT,
            "mijn account 2", 4321L, user, Currency.getInstance("EUR"));

    private void saveSetupAccounts() {
        userRepository.save(user);
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }

    @Test
    public void countTransactionsForStatusReturnsZeroIfThereAreNoTransactions() {
        int result = transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING);

        assertEquals(0, result);
    }

    @Test
    public void countTransactionsForStatusReturnsOneIfThereIsOnlyOne() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        int result = transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING);

        assertEquals(1, result);
    }

    @Test
    public void countTransactionsForStatusCountsMultiple() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction2);

        int result = transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING);

        assertEquals(2, result);
    }

    @Test
    public void countTransactionsForStatusOnlyCountsStatus() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.COMPLETED);

        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.FAILED);

        transactionRepository.save(transaction3);

        int result = transactionRepository.countTransactionsForStatus(TransactionStatus.PENDING);

        assertEquals(1, result);
    }

    @Test
    public void completeTransactionSetsTransactionStatusToCompleted() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        Transaction storedTransaction = transactionRepository.findById(transaction1.getId()).get();

        assertEquals(transaction1, storedTransaction);

        int updatedRows = transactionRepository.completeTransaction(transaction1.getId());

        assertEquals(1,updatedRows);

        Transaction updatedTransaction = transactionRepository.findById(transaction1.getId()).get();

        Transaction expectedTransaction = new Transaction(transaction1.getId(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.COMPLETED);

        assertEquals(expectedTransaction, updatedTransaction);
        assertEquals(expectedTransaction.getStatus(), updatedTransaction.getStatus());
    }

    @Test
    public void completeTransactionDoesNotUpdateWhenIdDoesNotExist() {
        int updatedRows = transactionRepository.completeTransaction(UUID.randomUUID());

        assertEquals(0,updatedRows);
    }

    @Test
    public void getPendingTransactionsGetsNothingIfThereIsNothing() {
        Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(
                TransactionStatus.PENDING, PageRequest.of(0, 10));
        assertEquals(Collections.emptyList(), transactions.getContent());
    }

    @Test
    public void getTransactionsForStatusGetsOneIfThereIsOnlyOne() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(
                TransactionStatus.PENDING, PageRequest.of(0, 10));
        assertEquals(Set.of(transaction1), Set.copyOf(transactions.getContent()));
    }

    @Test
    public void getTransactionsForStatusGetsMultiple() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction3);

        Page<Transaction> transactions = transactionRepository.getTransactionsForStatus(
                TransactionStatus.PENDING, PageRequest.of(0, 10));
        assertEquals(Set.of(transaction1, transaction2, transaction3), Set.copyOf(transactions.getContent()));
    }

    @Test
    public void getTransactionsForStatusOnlyGetsSpecifiedStatus() {
        saveSetupAccounts();

        Transaction transaction1 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 30L,
                Currency.getInstance("EUR"), "description", Instant.now(), TransactionStatus.PENDING);

        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.COMPLETED);

        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "description two", Instant.now(), TransactionStatus.FAILED);

        transactionRepository.save(transaction3);

        Page<Transaction> pendingTransactions = transactionRepository.getTransactionsForStatus(
                TransactionStatus.PENDING, PageRequest.of(0, 10));
        assertEquals(Set.of(transaction1), Set.copyOf(pendingTransactions.getContent()));

        Page<Transaction> failedTransactions = transactionRepository.getTransactionsForStatus(
                TransactionStatus.FAILED, PageRequest.of(0, 10));
        assertEquals(Set.of(transaction3), Set.copyOf(failedTransactions.getContent()));
    }

}