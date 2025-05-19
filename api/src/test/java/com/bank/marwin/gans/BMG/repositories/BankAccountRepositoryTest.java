package com.bank.marwin.gans.BMG.repositories;

import com.bank.marwin.gans.BMG.models.AccountType;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
public class BankAccountRepositoryTest {

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
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearTables() {
        bankAccountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findBankAccountByUserIdReturnsEmptyListWhenNothingFound() {
        List<BankAccount> result = bankAccountRepository.findByUserId(UUID.randomUUID());

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void findBankAccountByUserIdReturnsListWhenAnAccountFound() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 1234L, user,
                Currency.getInstance("EUR"));

        bankAccountRepository.save(account);

        List<BankAccount> result = bankAccountRepository.findByUserId(userId);

        assertEquals(List.of(account), result);
    }

    @Test
    void findBankAccountByUserIdReturnsListWhenAnAccountsFound() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 1234L, user,
                Currency.getInstance("EUR"));

        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount account2 = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2", 4321L,
                user, Currency.getInstance("EUR"));

        bankAccountRepository.save(account);
        bankAccountRepository.save(account2);

        List<BankAccount> result = bankAccountRepository.findByUserId(userId);

        assertEquals(Set.of(account, account2), Set.copyOf(result));
    }

    @Test
    void findBankAccountByIBANWhenNothingFoundReturnsEmptyOptional() {
        IBAN iban = new IBAN("NL12ABCD0123456789");

        Optional<BankAccount> result = bankAccountRepository.findByIban(iban);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void findBankAccountByIBANReturnsFilledOptional() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 1234L, user,
                Currency.getInstance("EUR"));

        bankAccountRepository.save(account);

        Optional<BankAccount> result = bankAccountRepository.findByIban(iban);

        assertEquals(Optional.of(account), result);
    }

    @Test
    void updateBalanceSetsTheBalanceToTheNewAmount() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 1234L, user,
                Currency.getInstance("EUR"));

        bankAccountRepository.save(account);

        Optional<BankAccount> storedAccount = bankAccountRepository.findById(accountId);
        assertEquals(account.getBalance(), storedAccount.get().getBalance());

        bankAccountRepository.updateBalance(accountId, account.getBalance() - 1233);

        Optional<BankAccount> updatedAccount = bankAccountRepository.findById(accountId);
        assertEquals(1, updatedAccount.get().getBalance());
    }

    @Test
    void updateBalanceWorksIfAmountIsEqual() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        userRepository.save(user);

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 1234L, user,
                Currency.getInstance("EUR"));

        bankAccountRepository.save(account);

        Optional<BankAccount> storedAccount = bankAccountRepository.findById(accountId);
        assertEquals(account.getBalance(), storedAccount.get().getBalance());

        bankAccountRepository.updateBalance(accountId, account.getBalance());

        Optional<BankAccount> updatedAccount = bankAccountRepository.findById(accountId);
        assertEquals(1234L, updatedAccount.get().getBalance());
    }
}