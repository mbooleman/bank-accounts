package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.domain.IBAN;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class IBANDto {
    @JsonProperty
    @Schema(example = "NL12BMG0123456789")
    private final String accountNumber;

    public IBANDto(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public IBAN toDomain() {
        return new IBAN(this.getAccountNumber());
    }
}
