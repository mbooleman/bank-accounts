package com.bank.marwin.gans.BMG.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private
    MockMvc mockMvc;

    @Test
    void whenPostOnUserEndpoint_thenReturnUserDtoAndOk() throws Exception {
        String input2 = """
                  {
                  "id": "5585bd7b-8206-478b-a55e-4da87d76bd0a",
                  "username": "theUser",
                  "email": "marwin@placeholder.nl",
                  "roles": [
                    "friend",
                    "brother"
                  ]
                }""";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(input2))
                .andExpect(status().isOk())
                .andExpect(content().json(input2));
    }

    @Test
    void whenPostOnUserEndpointWithoutId_thenReturnIsOk() throws Exception {
        String input2 = """
                  {
                  "username": "theUser",
                  "email": "marwin@placeholder.nl",
                  "roles": [
                    "friend",
                    "brother"
                  ]
                }""";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(input2))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostOnUserEndpointWithIncorrectUUID_then400BadRequest() throws Exception {
        String input2 = """
                  {
                  "id": "5585bd7b-8206-478b-a55e-4da876bd0a",
                  "username": "theUser",
                  "email": "marwin@placeholder.nl",
                  "roles": [
                    "friend",
                    "brother"
                  ]
                }""";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(input2))
                .andExpect(status().isBadRequest());
    }
}
