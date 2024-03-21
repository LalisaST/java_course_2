package edu.java.model.scheme;

import jakarta.validation.constraints.NotNull;

public record ChatLink(
    @NotNull Long linkId,
    @NotNull Long chatId
) {
}
