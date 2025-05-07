package com.bank.marwin.gans.BMG.models;

import com.bank.marwin.gans.BMG.errors.InvalidIBANAccountNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IBANTest {

    @Test
    void newIBANCreateWorksIfPatternMatches() {
        String accountNumber = "NL12INGB1234567890";

        assertDoesNotThrow(() -> new IBAN(accountNumber));
    }

    @Test
    void newIBANCreateWorksWithSpacesOrDashes() {
        String accountNumber = "NL-12INGB 1234567890";

        assertDoesNotThrow(() -> new IBAN(accountNumber));
    }

    @Test
    void newIBANCreateWorksWithNonCapitalizedLetters() {
        String accountNumber = "nl12ingb1234567890";

        assertDoesNotThrow(() -> new IBAN(accountNumber));
    }

    @Test
    void newIBANThrowsIfPatternDoesNotMatch() {
        String accountNumber = "NL12INGBSHOULDHAVENUMBERS";

        assertThrows(InvalidIBANAccountNumber.class, () -> new IBAN(accountNumber));
    }

    @Test
    void newIBANThrowsWhenSpecialCharacterIsUsed() {
        String accountNumber = "NL12INGB*&%$#@!";

        assertThrows(InvalidIBANAccountNumber.class, () -> new IBAN(accountNumber));
    }
}
