package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        User domainUser = user.toDomain();
        System.out.println(domainUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{name}")
    public ResponseEntity<UserDto> findUser(@PathVariable String name) {
        User user = new User(null, name, "email@email.com", Collections.emptyList());

        return ResponseEntity.ok(new UserDto(user));
    }

}


