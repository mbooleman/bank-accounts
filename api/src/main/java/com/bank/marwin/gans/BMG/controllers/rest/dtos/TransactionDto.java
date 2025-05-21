package com.bank.marwin.gans.BMG.controllers.rest.dtos;

import com.bank.marwin.gans.BMG.models.PreProcessingTransaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Currency;

public record TransactionDto(@JsonProperty IBANDto fromIban, @JsonProperty IBANDto toIban,
                             @JsonProperty @Schema(example = "description") String description,
                             @JsonProperty @Schema(example = "1234") Long amount,
                             @JsonProperty @Schema(example = "EUR") String currency) {

    public PreProcessingTransaction toDomain() {
        return new PreProcessingTransaction(this.fromIban.toDomain(), this.toIban.toDomain(), this.description,
                this.amount, Currency.getInstance(currency));
    }
}
