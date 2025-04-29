package com.bank.marwin.gans.domain;

import java.util.Currency;
import java.util.UUID;

public class BankAccount {
    private final UUID id;
    private final IBAN iban;
    private final AccountType accountType;
    private final String name;
    private final Long balance;
    private final UUID userId;
    private final Currency currency;

    public BankAccount(UUID id, IBAN iban, AccountType accountType, String name, Long balance, UUID userId, Currency currency) {
        this.id = id;
        this.iban = iban;
        this.accountType = accountType;
        this.name = name;
        this.balance = balance;
        this.userId = userId;
        this.currency = currency;
    }

    public UUID getId() {
        return id;
    }

    public IBAN getIban() {
        return iban;
    }

    public UUID getUserId() {
        return userId;
    }

    public Long getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Currency getCurrency() {
        return currency;
    }
}
