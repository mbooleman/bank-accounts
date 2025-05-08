package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.CombinedBankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record CombinedBankAccountDto(
        @JsonProperty @Schema(example = "5585bd7b-8206-478b-a55e-4da87d76bd0a") UUID userId,
        @JsonProperty @Schema(example = "[\"5585bd7b-8206-478b-a55e-4da87d76bd0b\", \"5585bd7b-8206-478b-a55e-4da87d76bd0c\"]") List<UUID> bankAccounts,
        @JsonProperty @Schema(example = "123.45") Double netBalance,
        @JsonProperty @Schema(example = "EUR") String currency) {

    public CombinedBankAccountDto(CombinedBankAccount combinedBankAccount) {
        this(combinedBankAccount.userId(), combinedBankAccount.bankAccounts().stream().map(BankAccount::getId).toList(),
                combinedBankAccount.netBalance(), combinedBankAccount.currency().getCurrencyCode());
    }
}
