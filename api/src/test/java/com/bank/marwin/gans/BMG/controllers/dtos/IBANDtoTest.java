package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.IBAN;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class IBANDtoTest {

    @Test
    void IBANToDtoWorks() {
        String accountNumber = "accountnumber";

        IBAN iban = new IBAN(accountNumber);

        IBANDto expectedDto = new IBANDto(accountNumber);

        IBANDto dto = new IBANDto(iban);

        assertEquals(expectedDto, dto);
    }

    @Test
    void IBANtoDomainWorks() {
        String accountNumber = "accountnumber";

        IBANDto dto = new IBANDto(accountNumber);

        IBAN expectedIBAN = new IBAN(accountNumber);

        IBAN iban = dto.toDomain();

        assertEquals(expectedIBAN.getAccountNumber(), iban.getAccountNumber());
        assertInstanceOf(IBAN.class, expectedIBAN);
    }
}
