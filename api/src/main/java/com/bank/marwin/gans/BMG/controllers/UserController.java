package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.CreateUserDto;
import com.bank.marwin.gans.BMG.controllers.dtos.UserResponseDto;
import com.bank.marwin.gans.BMG.errors.UserNotFoundException;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto userDto) {
        LOGGER.info("user creation request coming in");
        User user = userDto.toDomain();

        userService.createUser(user);

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("")
    public ResponseEntity<UserResponseDto> findUser(@RequestParam UUID userId) {
        LOGGER.info("find user for id {} request coming in", userId);

        User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        return ResponseEntity.ok(new UserResponseDto(user));
    }
}


