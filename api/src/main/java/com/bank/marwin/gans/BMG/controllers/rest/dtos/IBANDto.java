package com.bank.marwin.gans.BMG.controllers.rest.dtos;

import com.bank.marwin.gans.BMG.models.IBAN;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record IBANDto(@JsonProperty @Schema(example = "NL12BMG0123456789") String accountNumber) {

    public IBANDto(IBAN iban) {
       this(iban.getAccountNumber());
    }

    public IBAN toDomain() {
        return new IBAN(this.accountNumber());
    }
}
