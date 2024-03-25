package edu.java.model.scheme;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record Chat(
    @NotNull Long id,
    @NotNull OffsetDateTime createAt
) {
}
