package edu.java.scrapper.dto.scrapper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListLinksResponse(
    @NotEmpty List<LinkResponse> links,
    @NotNull Integer size
) {
}
