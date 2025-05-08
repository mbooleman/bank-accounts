package com.bank.marwin.gans.BMG.repositories;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Map;

@Service
public class CurrencyService {
    private final Map<Pair<Currency, Currency>, Double> exchangeTable = Map.ofEntries(
            Map.entry(Pair.of(Currency.getInstance("EUR"), Currency.getInstance("USD")), 1.19),
            Map.entry(Pair.of(Currency.getInstance("USD"), Currency.getInstance("EUR")), 1.0 / 1.19),
            Map.entry(Pair.of(Currency.getInstance("EUR"), Currency.getInstance("GBP")), 0.8),
            Map.entry(Pair.of(Currency.getInstance("GBP"), Currency.getInstance("EUR")), 1.0 / 0.8),
            Map.entry(Pair.of(Currency.getInstance("GBP"), Currency.getInstance("USD")), 1.4875),
            Map.entry(Pair.of(Currency.getInstance("USD"), Currency.getInstance("GBP")), 1.0 / 1.4875));

    public Double getExchangeRate(Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency.equals(toCurrency)) return 1.0;
        return exchangeTable.get(Pair.of(fromCurrency, toCurrency));
    }
}
