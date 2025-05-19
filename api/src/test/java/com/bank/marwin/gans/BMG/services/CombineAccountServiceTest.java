package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CombineAccountServiceTest {
    @InjectMocks
    private CombineAccountService combineAccountService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private BankAccountService accountService;

    @Test
    public void combineAccountsWorksWithTwoBankAccounts() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L, user,
                Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount account2 = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2", 200L,
                user, Currency.getInstance("EUR"));

        Mockito.when(accountService.getBankAccountsByUserId(userId)).thenReturn(List.of(account, account2));
        Mockito.when(currencyService.getExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("EUR")))
                .thenReturn(1.0);

        CombinedBankAccount result = combineAccountService.combineAccounts(userId, Currency.getInstance("EUR"));

        CombinedBankAccount expectedAccount = new CombinedBankAccount(userId, List.of(account, account2), 300.0,
                Currency.getInstance("EUR"));

        assertEquals(expectedAccount, result);
    }

    @Test
    public void combineAccountsWorksWithWhenCurrencyChanges() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT, "mijn account", 100L, user,
                Currency.getInstance("EUR"));
        IBAN iban2 = new IBAN("NL12ABCD0987654321");

        UUID accountId2 = UUID.randomUUID();
        BankAccount account2 = new BankAccount(accountId2, iban2, AccountType.CHECKING_ACCOUNT, "mijn account 2", 200L,
                user, Currency.getInstance("EUR"));

        Mockito.when(accountService.getBankAccountsByUserId(userId)).thenReturn(List.of(account, account2));
        Mockito.when(currencyService.getExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("USD")))
                .thenReturn(1.19);
        CombinedBankAccount result = combineAccountService.combineAccounts(userId, Currency.getInstance("USD"));

        CombinedBankAccount expectedAccount = new CombinedBankAccount(userId, List.of(account, account2), 300.0 * 1.19,
                Currency.getInstance("USD"));

        assertEquals(expectedAccount, result);
        assertEquals(expectedAccount.netBalance(), result.netBalance());
    }

    @Test
    public void combineAccountsDoesNotThrowWhenNoAccountExists() {
        UUID userId = UUID.randomUUID();

        Mockito.when(accountService.getBankAccountsByUserId(userId)).thenReturn(Collections.emptyList());

        CombinedBankAccount result = combineAccountService.combineAccounts(userId, Currency.getInstance("USD"));

        CombinedBankAccount expectedAccount = new CombinedBankAccount(userId, Collections.emptyList(), 0.0,
                Currency.getInstance("USD"));

        assertEquals(expectedAccount, result);
    }
}
