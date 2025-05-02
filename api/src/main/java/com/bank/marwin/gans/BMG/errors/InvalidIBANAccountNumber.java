package com.bank.marwin.gans.BMG.errors;

public class InvalidIBANAccountNumber extends RuntimeException {
    public InvalidIBANAccountNumber(String accountNumber) {
        super("Account number for IBAN " + accountNumber + " is not valid.");
    }
}
