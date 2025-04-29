package com.bank.marwin.gans.BMG.models;

import jakarta.persistence.*;

import java.util.Currency;
import java.util.UUID;

@Entity
public class BankAccount {

    @Id
    @Column(nullable = false, updatable = false)
    private final UUID id;

    @Column(nullable = false, unique = true)
    private final IBAN iban;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final AccountType accountType;

    @Column(nullable = false)
    private final String name;

    @Column(nullable = false)
    private final Long balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private final User user;

    @Column(nullable = false)
    private final Currency currency;

    public BankAccount(UUID id, IBAN iban, AccountType accountType, String name, Long balance, User user,
                       Currency currency) {
        this.id = id;
        this.iban = iban;
        this.accountType = accountType;
        this.name = name;
        this.balance = balance;
        this.user = user;
        this.currency = currency;
    }

    public UUID getId() {
        return id;
    }

    public IBAN getIban() {
        return iban;
    }

    public User getUser() {
        return user;
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
