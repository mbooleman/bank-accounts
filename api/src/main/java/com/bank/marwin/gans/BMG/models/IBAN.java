package com.bank.marwin.gans.BMG.models;

public class IBAN {
    private final String AccountNumber;

    public IBAN(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }
}
