package com.bank.marwin.gans.BMG.controllers.rest.dtos;

import com.bank.marwin.gans.BMG.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateUserDto(@JsonProperty @Schema(example = "theUser") @NotBlank String username,
                            @JsonProperty @Schema(example = "marwin@placeholder.nl") @Email String email,
                            @JsonProperty @Schema(example = "[\"friend\", \"brother\"]") @NotEmpty List<@NotBlank String> roles) {

    public User toDomain() {
        return new User(null, this.username, this.email, this.roles);
    }
}
