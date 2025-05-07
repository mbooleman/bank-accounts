package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.*;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import com.bank.marwin.gans.BMG.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class BankAccountServiceIntegrationTest {

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
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearTables() {
        bankAccountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void processTransactionUpdatesBothAccounts() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L, user,
                Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount account2 = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2", 200L,
                user, Currency.getInstance("EUR"));

        bankAccountService.createBankAccount(account);
        bankAccountService.createBankAccount(account2);

        Optional<BankAccount> result = bankAccountService.findBankAccount(accountId);
        Optional<BankAccount> result2 = bankAccountService.findBankAccount(accountId2);

        assertEquals(account, result.get());
        assertEquals(100, result.get().getBalance());
        assertEquals(account2, result2.get());
        assertEquals(200, result2.get().getBalance());

        Transaction transaction = new Transaction(UUID.randomUUID(), account2, account, 100L,
                Currency.getInstance("EUR"), "test transfer",
                Instant.now(), TransactionStatus.PENDING);

        bankAccountService.processTransaction(transaction);

        Optional<BankAccount> updateResult = bankAccountService.findBankAccount(accountId);
        Optional<BankAccount> updateResult2 = bankAccountService.findBankAccount(accountId2);

        assertEquals(account, updateResult.get());
        assertEquals(200, updateResult.get().getBalance());
        assertEquals(account2, updateResult2.get());
        assertEquals(100, updateResult2.get().getBalance());
    }
}