package com.bank.marwin.gans.BMG.controllers.errors;

import com.bank.marwin.gans.BMG.models.IBAN;

import java.util.UUID;

public class BankAccountNotFoundByIBANException extends NotFoundException {
    public BankAccountNotFoundByIBANException(IBAN iban) {
        super("Bank Account with IBAN  " + iban.getAccountNumber() + " not found.");
    }
}
