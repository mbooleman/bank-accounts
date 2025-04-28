package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {

    @Test
    void toDomainCreatesNewUserWhenIdPresent() {
        UserDto dto = new UserDto(UUID.fromString("5585bd7b-8206-478b-a55e-4da87d76bd0a"), "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));

        User user = dto.toDomain();
        User expectedUser = new User(UUID.fromString("5585bd7b-8206-478b-a55e-4da87d76bd0a"), "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));
        assertEquals(user.getId(), expectedUser.getId());
        assertEquals(user.getEmail(), expectedUser.getEmail());
        assertEquals(user.getRoles(), expectedUser.getRoles());
        assertEquals(user.getUsername(), expectedUser.getUsername());
    }

    @Test
    void toDomainCreatesNewUserWhenIdNotPresentPresent() {
        UserDto dto = new UserDto(null, "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));

        User user = dto.toDomain();
        User expectedUser = new User(user.getId(), "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));
        assertEquals(user.getId(), expectedUser.getId());
        assertNotNull(user.getId());
        assertEquals(user.getEmail(), expectedUser.getEmail());
        assertEquals(user.getRoles(), expectedUser.getRoles());
        assertEquals(user.getUsername(), expectedUser.getUsername());
    }
}
