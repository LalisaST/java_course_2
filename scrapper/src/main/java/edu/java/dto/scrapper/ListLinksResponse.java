package edu.java.dto.scrapper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListLinksResponse(
    @NotEmpty List<LinkResponse> links,
    @NotNull int size
) {
}
