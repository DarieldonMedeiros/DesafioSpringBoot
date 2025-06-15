package com.zipdin.avaliacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse")
public record LoginResponseDTO(String token) {

}
