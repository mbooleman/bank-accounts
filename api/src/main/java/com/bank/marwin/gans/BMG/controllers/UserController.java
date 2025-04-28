package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {

    @GetMapping
    public String thing() {
        return "this hello";
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        User domainUser = user.toDomain();
        System.out.println(domainUser.toString());
        return ResponseEntity.ok(user);
    }
}


