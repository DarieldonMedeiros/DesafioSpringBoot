package com.zipdin.avaliacao.dto;

import java.time.OffsetDateTime;
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
    OffsetDateTime releasedAt
) {}
