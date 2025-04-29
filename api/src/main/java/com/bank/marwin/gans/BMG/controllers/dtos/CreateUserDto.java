package com.bank.marwin.gans.BMG.controllers.dtos;

import com.bank.marwin.gans.BMG.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateUserDto(@JsonProperty @Schema(example = "theUser") String username,
                            @JsonProperty @Schema(example = "marwin@placeholder.nl") String email,
                            @JsonProperty @Schema(example = "[\"friend\", \"brother\"]") List<String> roles) {

    public User toDomain() {
        return new User(null, this.username, this.email, this.roles);
    }
}
