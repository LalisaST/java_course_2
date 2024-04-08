package edu.java.scrapper.dto.scrapper;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record RemoveLinkRequest(
    @NotNull URI url
) {
}
