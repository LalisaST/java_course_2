package edu.java.dto.scrapper;

import java.net.URI;
import org.jetbrains.annotations.NotNull;

public record LinkResponse(
    @NotNull Long id,
    @NotNull URI url
) {

}
