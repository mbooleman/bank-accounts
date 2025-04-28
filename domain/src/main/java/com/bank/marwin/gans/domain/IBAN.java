package com.bank.marwin.gans.domain;

public class IBAN {
    private final String AccountNumber;

    public IBAN(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }
}
