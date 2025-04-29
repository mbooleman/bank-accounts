package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.AccountType;
import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Currency;
import java.util.UUID;

public record CreateBankAccountDto(@JsonProperty IBANDto iban,
                                   @JsonProperty @Schema(example = "SAVINGS_ACCOUNT") String accountType,
                                   @JsonProperty @Schema(example = "mijn account naam") String name,
                                   @JsonProperty @Schema(example = "1234") Long balance,
                                   @JsonProperty @Schema(example = "5585bd7b-8206-478b-a55e-4da87d76bd0a") UUID userId,
                                   @JsonProperty @Schema(example = "EUR") String currency) {

    public BankAccount toDomain(User user) {
        return new BankAccount(null, this.iban.toDomain(), AccountType.valueOf(this.accountType), this.name,
                this.balance, user, Currency.getInstance(currency));
    }
}
