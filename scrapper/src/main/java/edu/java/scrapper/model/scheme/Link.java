package edu.java.scrapper.model.scheme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    @NotNull Long id,
    @NotBlank URI url,
    @NotNull OffsetDateTime lastUpdate,
    @NotNull OffsetDateTime lastCheck,
    @NotNull Type type,
    Integer commitCount,
    Integer answerCount,
    Integer commentCount
) {
}
