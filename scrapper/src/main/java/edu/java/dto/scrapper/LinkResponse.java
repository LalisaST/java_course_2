package edu.java.dto.scrapper;

import jakarta.validation.constraints.NotNull;
import java.net.URI;


public record LinkResponse(
    @NotNull Long id,
    @NotNull URI url
) {

}
