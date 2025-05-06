package com.bank.marwin.gans.BMG.models;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public record PreProcessingTransaction(IBAN fromIban, IBAN toIban, String description, Long amount, Currency currency) {

    public Transaction toTransaction(BankAccount fromAccount, BankAccount toAccount) {
        return new Transaction(UUID.randomUUID(), fromAccount, toAccount, this.amount, this.currency, this.description,
                Instant.now(), TransactionStatus.PENDING);
    }
}
