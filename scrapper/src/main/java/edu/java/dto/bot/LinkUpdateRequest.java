package edu.java.dto.bot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LinkUpdateRequest(
    @NotNull Long id,
    @NotNull URI url,
    @NotBlank String description,
    @NotEmpty List<Long> tgChatIds
) {
}
