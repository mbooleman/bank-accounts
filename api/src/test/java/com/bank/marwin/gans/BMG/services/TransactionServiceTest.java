package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.errors.BankAccountNotFoundByIBANException;
import com.bank.marwin.gans.BMG.models.*;
import com.bank.marwin.gans.BMG.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
public class TransactionServiceTest {

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
    private TransactionService transactionService;

    @MockitoBean
    private TransactionRepository transactionRepository;

    @MockitoBean
    private BankAccountService accountService;

    @Test
    void createTransactionWorksIfAccountsExist() {
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

        String description = "test transfer";
        Long amount = 100L;
        Currency currency = Currency.getInstance("EUR");

        PreProcessingTransaction preProcessingTransaction = new PreProcessingTransaction(iban2, iban, description,
                amount, currency);

        Mockito.when(accountService.findBankAccountByIBAN(fromAccount.getIban())).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountService.findBankAccountByIBAN(toAccount.getIban())).thenReturn(Optional.of(toAccount));

        Transaction expectedTransaction = new Transaction(UUID.randomUUID(), fromAccount, toAccount, amount, currency,
                description, Instant.now(), TransactionStatus.PENDING);

        Mockito.when(transactionRepository.save(any())).thenReturn(expectedTransaction);

        Transaction result = transactionService.createTransaction(preProcessingTransaction);

        assertEquals(expectedTransaction, result);
    }

    @Test
    void createTransactionThrowsIfFromBankAccountDoesNotExist() {
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

        String description = "test transfer";
        Long amount = 100L;
        Currency currency = Currency.getInstance("EUR");

        PreProcessingTransaction preProcessingTransaction = new PreProcessingTransaction(iban2, iban, description,
                amount, currency);

        Mockito.when(accountService.findBankAccountByIBAN(fromAccount.getIban())).thenReturn(Optional.ofNullable(null));
        Mockito.when(accountService.findBankAccountByIBAN(toAccount.getIban())).thenReturn(Optional.of(toAccount));

        BankAccountNotFoundByIBANException exception = assertThrows(BankAccountNotFoundByIBANException.class,
                () -> transactionService.createTransaction(preProcessingTransaction));

        assertTrue(exception.getMessage().contains("Bank Account with IBAN  " + iban2.getAccountNumber() + " not found."));
        verify(transactionRepository, times(0)).save(any());
        verify(accountService, times(0)).findBankAccountByIBAN(toAccount.getIban());
    }

    @Test
    void createTransactionThrowsIfToBankAccountDoesNotExist() {
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

        String description = "test transfer";
        Long amount = 100L;
        Currency currency = Currency.getInstance("EUR");

        PreProcessingTransaction preProcessingTransaction = new PreProcessingTransaction(iban2, iban, description,
                amount, currency);

        Mockito.when(accountService.findBankAccountByIBAN(fromAccount.getIban())).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountService.findBankAccountByIBAN(toAccount.getIban())).thenReturn(Optional.ofNullable(null));

        BankAccountNotFoundByIBANException exception = assertThrows(BankAccountNotFoundByIBANException.class,
                () -> transactionService.createTransaction(preProcessingTransaction));

        assertTrue(exception.getMessage().contains("Bank Account with IBAN  " + iban.getAccountNumber() + " not found."));

        verify(accountService, times(1)).findBankAccountByIBAN(fromAccount.getIban());
        verify(transactionRepository, times(0)).save(any());
    }
}

