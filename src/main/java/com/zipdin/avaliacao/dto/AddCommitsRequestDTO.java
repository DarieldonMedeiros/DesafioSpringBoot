package com.zipdin.avaliacao.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "AddCommitsRequest")
public record AddCommitsRequestDTO(@NotEmpty(message = "Lista de commits não pode ser vazia") List<String> commits) {

}
