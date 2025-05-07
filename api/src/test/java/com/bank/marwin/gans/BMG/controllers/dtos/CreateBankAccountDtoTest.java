package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.AccountType;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.IBAN;
import com.bank.marwin.gans.BMG.models.User;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateBankAccountDtoTest {

    @Test
    void toDomainCreatesNewBankAccount() {
        IBANDto ibanDto = new IBANDto("NL12INGB1234567890");
        UUID userId = UUID.randomUUID();
        CreateBankAccountDto dto = new CreateBankAccountDto(ibanDto, "SAVINGS_ACCOUNT", "marwin", 1234L, userId, "EUR");

        User user = new User(userId, "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));

        BankAccount bankAccount = dto.toDomain(user);

        BankAccount expectedBankAccount = new BankAccount(bankAccount.getId(), new IBAN("NL12INGB1234567890"),
                AccountType.SAVINGS_ACCOUNT, "marwin", 1234L, user, Currency.getInstance("EUR"));

        assertEquals(expectedBankAccount.getId(), bankAccount.getId());
        assertNotNull(bankAccount.getId());
        assertEquals(expectedBankAccount.getAccountType(), bankAccount.getAccountType());
        assertEquals(expectedBankAccount.getName(), bankAccount.getName());
        assertEquals(expectedBankAccount.getBalance(), bankAccount.getBalance());
        assertEquals(expectedBankAccount.getUser(), bankAccount.getUser());
        assertEquals(expectedBankAccount.getCurrency(), bankAccount.getCurrency());
    }
}
