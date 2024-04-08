package edu.java.scrapper.model.scheme;

import jakarta.validation.constraints.NotNull;

public record ChatLink(
    @NotNull Long linkId,
    @NotNull Long chatId
) {
}
