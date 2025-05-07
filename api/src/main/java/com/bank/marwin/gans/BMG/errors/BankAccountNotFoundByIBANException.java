package com.bank.marwin.gans.BMG.errors;

import com.bank.marwin.gans.BMG.models.IBAN;

public class BankAccountNotFoundByIBANException extends NotFoundException {
    public BankAccountNotFoundByIBANException(IBAN iban) {
        super("Bank Account with IBAN  " + iban.getAccountNumber() + " not found.");
    }
}
