package com.bank.marwin.gans.BMG.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
import java.util.UUID;

@Entity
public class User {

    @Id
    @Column(updatable = false, nullable = false)
    private final UUID id;


    @Column(updatable = false, nullable = false)
    private final String username;

    @Column(nullable = false)
    private final String email;

    @Column(nullable = false)
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
