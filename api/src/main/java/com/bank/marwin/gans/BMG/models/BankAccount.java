package com.bank.marwin.gans.BMG.models;

import com.bank.marwin.gans.BMG.models.converters.IBANConverter;
import jakarta.persistence.*;

import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Convert(converter = IBANConverter.class)
    private IBAN iban;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Currency currency;

    protected BankAccount() {
    }

    public BankAccount(UUID id, IBAN iban, AccountType accountType, String name, Long balance, User user,
                       Currency currency) {

        this.id = id == null ? UUID.randomUUID() : id;
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
