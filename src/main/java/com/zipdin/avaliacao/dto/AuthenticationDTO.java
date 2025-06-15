package com.zipdin.avaliacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Authentication")
public record AuthenticationDTO(String login, String password) {

}
