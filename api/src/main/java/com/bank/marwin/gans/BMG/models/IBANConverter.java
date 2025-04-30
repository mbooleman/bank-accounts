package com.bank.marwin.gans.BMG.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class IBANConverter implements AttributeConverter<IBAN, String> {

    @Override
    public String convertToDatabaseColumn(IBAN iban) {
        return iban != null ? iban.getAccountNumber() : null;
    }

    @Override
    public IBAN convertToEntityAttribute(String dbData) {
        return dbData != null ? new IBAN(dbData) : null;
    }
}
