package com.bank.marwin.gans.BMG.services;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Currency;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CurrencyServiceTest {
    private final CurrencyService currencyService = new CurrencyService();

    static Stream<Triple<Currency, Currency, Double>> currenciesAndRates() {
        return Stream.of(
                Triple.of(Currency.getInstance("EUR"), Currency.getInstance("USD"), 1.19),
                Triple.of(Currency.getInstance("USD"), Currency.getInstance("EUR"), 1.0 / 1.19),
                Triple.of(Currency.getInstance("EUR"), Currency.getInstance("GBP"), 0.8),
                Triple.of(Currency.getInstance("GBP"), Currency.getInstance("EUR"), 1.0 / 0.8),
                Triple.of(Currency.getInstance("GBP"), Currency.getInstance("USD"), 1.4875),
                Triple.of(Currency.getInstance("USD"), Currency.getInstance("GBP"), 1.0 / 1.4875)
        );
    }

    @Test
    public void getExchangeRateReturnsDoubleOfExchangeRateOfCurrencyCombination() {
        Double result = currencyService.getExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("USD"));

        assertEquals(1.19, result);
    }

    @ParameterizedTest
    @MethodSource("currenciesAndRates")
    void testAllAllowedCurrencyCombinations(Triple<Currency, Currency, Double> currenciesAndRates) {
        Currency from = currenciesAndRates.getLeft();
        Currency to = currenciesAndRates.getMiddle();
        Double expectedRate = currenciesAndRates.getRight();

        Double result = currencyService.getExchangeRate(from, to);

        assertEquals(expectedRate, result);
    }

    @Test
    public void getExchangeRateReturnsNullIfCombinationDoesNotExist() {
        Double result = currencyService.getExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("JPY"));

        assertNull(result);
    }

    @Test
    public void getExchangeRateReturnsOneIfBothCurrenciesAreEqual() {
        Double result = currencyService.getExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("EUR"));

        assertEquals(1.0, result);
    }
}
