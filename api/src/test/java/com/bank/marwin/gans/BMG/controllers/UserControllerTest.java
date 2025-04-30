package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.BMG.controllers.dtos.UserResponseDto;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.services.BankAccountService;
import com.bank.marwin.gans.BMG.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void whenPostOnUserEndpoint_thenReturnUserDtoAndOk() throws Exception {
        String input = """
                  {
                  "username": "theUser",
                  "email": "marwin@placeholder.nl",
                  "roles": [
                    "friend",
                    "brother"
                  ]
                }""";


        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isOk())
                .andExpect(content().json(input));
    }

    @Test
    void whenGetOnUserEndpoint_thenReturnUserDtoAndOk() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "marwin", "marwin@place.com", List.of("abc", "bcd"));

        String expectedResponse = String.format("""
                  {
                  "id": "%s",
                  "username": "marwin",
                  "email": "marwin@place.com",
                  "roles": [
                    "abc",
                    "bcd"
                  ]
                }""", userId);


        when(userService.findUserById(userId)).thenAnswer(id -> Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?userId=" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void whenGetOnUserEndpoint_thenReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userService.findUserById(userId)).thenAnswer(id -> Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?userId=" + userId))
                .andExpect(status().isNotFound());
    }
}
