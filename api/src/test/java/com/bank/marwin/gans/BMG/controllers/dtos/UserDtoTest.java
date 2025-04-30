package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {

    @Test
    void toDomainCreatesNewUserWhenIdNotPresentPresent() {
        CreateUserDto dto = new CreateUserDto("marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));

        User user = dto.toDomain();
        User expectedUser = new User(user.getId(), "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));
        assertEquals(user.getId(), expectedUser.getId());
        assertNotNull(user.getId());
        assertEquals(user.getEmail(), expectedUser.getEmail());
        assertEquals(user.getRoles(), expectedUser.getRoles());
        assertEquals(user.getUsername(), expectedUser.getUsername());
    }
}
