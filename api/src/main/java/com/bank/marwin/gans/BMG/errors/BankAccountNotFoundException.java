package com.bank.marwin.gans.BMG.errors;

import java.util.UUID;

public class BankAccountNotFoundException extends NotFoundException {
    public BankAccountNotFoundException(UUID bankAccountId) {
        super("Bank Account with id " + bankAccountId.toString() + " not found.");
    }
}
