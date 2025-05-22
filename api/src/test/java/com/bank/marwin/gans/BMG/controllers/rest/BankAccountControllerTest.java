package com.bank.marwin.gans.BMG.controllers.rest;

import com.bank.marwin.gans.BMG.models.AccountType;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.CombineAccountService;
import com.bank.marwin.gans.BMG.services.BankAccountService;
import com.bank.marwin.gans.BMG.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BankAccountController.class)
public class BankAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankAccountService bankAccountService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CombineAccountService combiner;

    @Test
    void whenPostOnBankAccountEndpoint_andUserExists_thenReturnBankAccountDtoAndOk() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        String input = String.format("""
                  {
                  "iban": {
                        "accountNumber": "NL12ABCD0123456789"
                  },
                  "accountType": "SAVINGS_ACCOUNT",
                  "name": "mijn account",
                  "balance": 1234,
                  "userId": "%s",
                  "currency": "EUR"
                  }""", userId);

        when(userService.findUserById(userId)).thenAnswer(id -> Optional.of(user));


        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isOk())
                .andExpect(content().json(input));
    }

    @Test
    void whenPostOnBankAccountEndpoint_andUserExists_ButIbanIsInvalid() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        String invalidAccount = "invalid";

        String input = String.format("""
                  {
                  "iban": {
                        "accountNumber": "%s"
                  },
                  "accountType": "SAVINGS_ACCOUNT",
                  "name": "mijn account",
                  "balance": 1234,
                  "userId": "%s",
                  "currency": "EUR"
                  }""", invalidAccount, userId);

        when(userService.findUserById(userId)).thenAnswer(id -> Optional.of(user));


        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Account number for IBAN " + invalidAccount + " is not valid."));
    }

    @Test
    void whenPostOnBankAccountEndpoint_andUserDoesNotExists_thenReturnBankAccountDtoAndOk() throws Exception {
        UUID userId = UUID.randomUUID();

        String input = String.format("""
                  {
                  "iban": {
                        "accountNumber": "NL12ABCD0123456789"
                  },
                  "accountType": "SAVINGS_ACCOUNT",
                  "name": "mijn account",
                  "balance": 1234,
                  "userId": "%s",
                  "currency": "EUR"
                }""", userId);

        when(userService.findUserById(userId)).thenAnswer(id -> Optional.ofNullable(null));


        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id " + userId + " not found."));
    }

    @Test
    void whenGetOnByIban_andIsFound_thenReturnAccountDtoAndOk() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT,
                "mijn account", 1234L, user, Currency.getInstance("EUR"));

        String expectedResponse = String.format("""
                  {
                  "id": "%s",
                  "iban": {
                        "accountNumber": "NL12ABCD0123456789"
                  },
                  "accountType": "SAVINGS_ACCOUNT",
                  "name": "mijn account",
                  "balance": 1234,
                  "userId": "%s",
                  "currency": "EUR"
                }""", accountId, userId);


        when(bankAccountService.findBankAccountByIBAN(any())).thenAnswer(id -> Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/" + iban.getAccountNumber()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void whenGetOnByIban_andIsNotFound_thenReturnNotFound() throws Exception {
        IBAN iban = new IBAN("NL12ABCD0123456789");

        when(bankAccountService.findBankAccountByIBAN(iban)).thenAnswer(id -> Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/" + iban.getAccountNumber()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bank Account with IBAN  " + iban.getAccountNumber() + " not found."));
    }

    @Test
    void whenGetOnByIban_andTheAccountNumberIsNotValid_thenReturnUnprocessableEntity() throws Exception {
        String invalidAccount = "NL12ABCD012345678**";


        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/" + invalidAccount))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Account number for IBAN " + invalidAccount + " is not valid."));
    }

    @Test
    void whenGetById_andIsFound_thenReturnAccountDtoAndOk() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        IBAN iban = new IBAN("NL12ABCD0123456789");

        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, iban, AccountType.SAVINGS_ACCOUNT,
                "mijn account", 1234L, user, Currency.getInstance("EUR"));

        String expectedResponse = String.format("""
                  {
                  "id": "%s",
                  "iban": {
                        "accountNumber": "NL12ABCD0123456789"
                  },
                  "accountType": "SAVINGS_ACCOUNT",
                  "name": "mijn account",
                  "balance": 1234,
                  "userId": "%s",
                  "currency": "EUR"
                }""", accountId, userId);


        when(bankAccountService.findBankAccount(accountId)).thenAnswer(id -> Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts?bankAccountId=" + accountId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void whenGetById_andIsNotFound_thenReturnAccountDtoAndOk() throws Exception {
        UUID accountId = UUID.randomUUID();

        when(bankAccountService.findBankAccount(accountId)).thenAnswer(id -> Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts?bankAccountId=" + accountId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bank Account with id " + accountId + " not found."));
    }
}
