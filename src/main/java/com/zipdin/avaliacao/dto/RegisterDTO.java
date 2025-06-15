package com.zipdin.avaliacao.dto;

import com.zipdin.avaliacao.entities.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
