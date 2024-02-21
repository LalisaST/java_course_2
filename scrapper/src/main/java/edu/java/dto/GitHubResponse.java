package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.OffsetDateTime;

public record GitHubResponse(
    String name,

    @JsonAlias("updated_at")
    OffsetDateTime update,

    @JsonAlias("html_url")
    String html
) {
}
