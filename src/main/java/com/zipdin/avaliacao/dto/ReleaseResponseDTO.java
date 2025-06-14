package com.zipdin.avaliacao.dto;

import java.time.LocalDateTime;
import java.util.List;

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
