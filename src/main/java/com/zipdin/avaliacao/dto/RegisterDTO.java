package com.zipdin.avaliacao.dto;

import com.zipdin.avaliacao.entities.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Register")
public record RegisterDTO(String login, String password, UserRole role) {

}
