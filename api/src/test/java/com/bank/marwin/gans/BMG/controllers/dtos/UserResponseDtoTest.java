package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResponseDtoTest {

    @Test
    void creatingUserResponseDtoTestFromUserWorks() {
        User user = new User(null, "marwin", "marwin@placeholder.nl", List.of("rol a", "rol b"));

        UserResponseDto expectedDto = new UserResponseDto(user.getId(), "marwin", "marwin@placeholder.nl",
                List.of("rol a", "rol b"));

        UserResponseDto result = new UserResponseDto(user);

        assertEquals(expectedDto, result);
    }
}
