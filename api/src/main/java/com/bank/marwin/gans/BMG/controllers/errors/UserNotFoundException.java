package com.bank.marwin.gans.BMG.controllers.errors;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User with id " + userId.toString() + " not found.");
    }
}
