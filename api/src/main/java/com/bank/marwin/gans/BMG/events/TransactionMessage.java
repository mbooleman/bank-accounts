package com.bank.marwin.gans.BMG.events;

import java.util.UUID;

public record TransactionMessage(UUID transactionId, Long amount) {
    @Override
    public String toString() {
        return "transactionId:" + transactionId + ", amount=" + amount;
    }
}
