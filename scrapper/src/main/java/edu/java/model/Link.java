package edu.java.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    @NotNull Long id,
    @NotBlank URI url,
    @NotNull OffsetDateTime lastUpdate,
    @NotNull OffsetDateTime lastCheck
) {
}
