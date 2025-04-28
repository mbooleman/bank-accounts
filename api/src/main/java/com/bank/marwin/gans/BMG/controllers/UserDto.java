package com.bank.marwin.gans.BMG.controllers;

import com.bank.marwin.gans.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record UserDto(@JsonProperty @Schema(example = "5585bd7b-8206-478b-a55e-4da87d76bd0a") UUID id,
                      @JsonProperty @Schema(example = "theUser") String username,
                      @JsonProperty @Schema(example = "marwin@placeholder.nl") String email,
                      @JsonProperty @Schema(example = "[\"friend\", \"brother\"]") List<String> roles) {

    public User toDomain() {
        return this.id == null ? new User(UUID.randomUUID(), this.username, this.email, this.roles) :
                new User(this.id, this.username, this.email, this.roles);
    }
}
