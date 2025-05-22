package com.bank.marwin.gans.BMG.controllers.rest.dtos;

import java.util.List;

public class UserGraph {
    private final String id;
    private final String username;
    private final String email;
    private final List<String> roles;

    public UserGraph(String id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
