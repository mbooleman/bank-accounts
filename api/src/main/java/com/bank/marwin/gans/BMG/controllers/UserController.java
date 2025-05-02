package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.CreateUserDto;
import com.bank.marwin.gans.BMG.controllers.dtos.UserResponseDto;
import com.bank.marwin.gans.BMG.errors.UserNotFoundException;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserDto userDto) {
        User user = userDto.toDomain();

        userService.createUser(user);

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("")
    public ResponseEntity<UserResponseDto> findUser(@RequestParam UUID userId) {

        User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        return ResponseEntity.ok(new UserResponseDto(user));
    }
}


