package com.bank.marwin.gans.BMG.models;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

public record CombinedBankAccount(UUID userId, List<BankAccount> bankAccounts, Double netBalance, Currency currency) {
}
