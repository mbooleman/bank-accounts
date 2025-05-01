package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record BankAccountResponseAccountDto(@JsonProperty @Schema(example = "5585bd7b-8206-478b-a55e-4da87d76bd0a") UUID id,
                                            @JsonProperty IBANDto iban,
                                            @JsonProperty @Schema(example = "SAVINGS_ACCOUNT") String accountType,
                                            @JsonProperty @Schema(example = "mijn account naam") String name,
                                            @JsonProperty @Schema(example = "1234") Long balance,
                                            @JsonProperty @Schema(example = "5585bd7b-8206-478b-a55e-4da87d76bd0a") UUID userId,
                                            @JsonProperty @Schema(example = "EUR") String currency) {

    public BankAccountResponseAccountDto(BankAccount bankAccount) {
        this(bankAccount.getId(), new IBANDto(bankAccount.getIban()),
                bankAccount.getAccountType().name(), bankAccount.getName(),
                bankAccount.getBalance(), bankAccount.getUser().getId(), bankAccount.getCurrency().getCurrencyCode());
    }
}
