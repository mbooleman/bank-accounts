package com.bank.marwin.gans.BMG.errors;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID userId) {
        super("User with id " + userId.toString() + " not found.");
    }
}
