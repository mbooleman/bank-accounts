package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.*;
import com.bank.marwin.gans.BMG.repositories.BankAccountRepository;
import com.bank.marwin.gans.BMG.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private KafkaService kafkaService;

    @BeforeEach
    public void clearTables() {
        bankAccountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void processTransactionUpdatesOnlyFromAccounts() { //to account updates from kafka
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID toAccountId = UUID.randomUUID();
        BankAccount toAccount = new BankAccount(toAccountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L, user,
                Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID fromAccountId = UUID.randomUUID();
        BankAccount fromAccount = new BankAccount(fromAccountId, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2", 200L,
                user, Currency.getInstance("EUR"));

        bankAccountService.createBankAccount(toAccount);
        bankAccountService.createBankAccount(fromAccount);

        Optional<BankAccount> toResult = bankAccountService.findBankAccount(toAccountId);
        Optional<BankAccount> fromResult = bankAccountService.findBankAccount(fromAccountId);

        assertEquals(toAccount, toResult.get());
        assertEquals(100, toResult.get().getBalance());
        assertEquals(fromAccount, fromResult.get());
        assertEquals(200, fromResult.get().getBalance());

        Transaction transaction = new Transaction(UUID.randomUUID(), fromAccount, toAccount, 100L,
                Currency.getInstance("EUR"), "test transfer",
                Instant.now(), TransactionStatus.PENDING);

        bankAccountService.processTransaction(transaction);

        Optional<BankAccount> toUpdateResult = bankAccountService.findBankAccount(toAccountId);
        Optional<BankAccount> fromUpdateResult = bankAccountService.findBankAccount(fromAccountId);

        assertEquals(toAccount, toUpdateResult.get());
        assertEquals(100, toUpdateResult.get().getBalance());
        assertEquals(fromAccount, fromUpdateResult.get());
        assertEquals(100, fromUpdateResult.get().getBalance());
    }
}