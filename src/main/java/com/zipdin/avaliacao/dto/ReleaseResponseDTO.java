package com.zipdin.avaliacao.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReleaseResponse")
public record ReleaseResponseDTO(
    String message,
    Long id,
    String system,
    String version,
    List<String> commits,
    String notes,
    String user,
    String userUpdate,
    LocalDateTime releasedAt
) {}
