package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.models.BankAccount;
import com.bank.marwin.gans.BMG.models.CombinedBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Service
public class CombineAccountService {
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private BankAccountService bankAccountService;

    public CombinedBankAccount combineAccounts(UUID userId, Currency currency) {
        List<BankAccount> accounts = bankAccountService.getBankAccountsByUserId(userId);
        Double netBalance = accounts.stream()
                .map(account -> convertCurrency(account, currency))
                .reduce(0.0, Double::sum);
        return new CombinedBankAccount(userId, accounts, netBalance, currency);
    }

    private Double convertCurrency(BankAccount account, Currency currency) {
        Double exchangeRate = currencyService.getExchangeRate(account.getCurrency(), currency);
        return account.getBalance() * exchangeRate;
    }
}
