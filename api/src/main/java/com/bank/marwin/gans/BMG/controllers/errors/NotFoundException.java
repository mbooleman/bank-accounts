package com.bank.marwin.gans.BMG.controllers.errors;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}