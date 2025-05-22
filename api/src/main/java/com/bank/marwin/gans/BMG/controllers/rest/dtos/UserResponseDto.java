package com.bank.marwin.gans.BMG.controllers.rest.dtos;

import com.bank.marwin.gans.BMG.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record UserResponseDto(@JsonProperty @Schema(example = "5585bd7b-8206-478b-a55e-4da87d76bd0a") UUID id,
                       @JsonProperty @Schema(example = "theUser") String username,
                       @JsonProperty @Schema(example = "marwin@placeholder.nl") String email,
                       @JsonProperty @Schema(example = "[\"friend\", \"brother\"]") List<String> roles) {

    public UserResponseDto(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }
}

