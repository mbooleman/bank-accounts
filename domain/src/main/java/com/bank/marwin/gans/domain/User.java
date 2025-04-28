package com.bank.marwin.gans.domain;

import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String email;
    private final List<String> roles;

    public User(UUID id, String username, String email, List<String> roles) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
