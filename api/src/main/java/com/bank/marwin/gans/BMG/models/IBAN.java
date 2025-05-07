package com.bank.marwin.gans.BMG.models;

import com.bank.marwin.gans.BMG.errors.InvalidIBANAccountNumber;

public class IBAN {
    private final String AccountNumber;
    private static final String REGEX_PATTERN_VALIDATION = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{3,5}[0-9]{10,23}$";
    private static final String REGEX_PATTERN_SANITIZE = "[ -]";

    public IBAN(String accountNumber) {
        String ibanSanitized = accountNumber.toUpperCase().replaceAll(REGEX_PATTERN_SANITIZE, "");

        if (!ibanSanitized.matches(REGEX_PATTERN_VALIDATION)) {
            throw new InvalidIBANAccountNumber(accountNumber);
        };

        AccountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }
}
