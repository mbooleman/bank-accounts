package com.bank.marwin.gans.BMG.controllers.rest.dtos;

import com.bank.marwin.gans.BMG.models.AccountType;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.models.User;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankAccountResponseDtoTest {

    @Test
    void creatingBankAccountResponseDtoTestFromBankAccountWorks() {
        UUID userId = UUID.randomUUID();
        UUID bankAccountId = UUID.randomUUID();

        User user = new User(userId, "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));


        BankAccount bankAccount = new BankAccount(bankAccountId, new IBAN("NL12INGB1234567890"),
                AccountType.SAVINGS_ACCOUNT, "marwin", 1234L, user, Currency.getInstance("EUR"));

        BankAccountResponseAccountDto expectedDto = new BankAccountResponseAccountDto(bankAccountId,
                new IBANDto("NL12INGB1234567890"), "SAVINGS_ACCOUNT", "marwin", 1234L, user.getId(), "EUR");

        BankAccountResponseAccountDto result = new BankAccountResponseAccountDto(bankAccount);

        assertEquals(expectedDto, result);
    }
}
