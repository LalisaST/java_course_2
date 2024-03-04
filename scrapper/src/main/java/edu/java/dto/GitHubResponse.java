package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.OffsetDateTime;

public record GitHubResponse(
    @JsonAlias("html_url")
    String html,

    @JsonAlias("updated_at")
    OffsetDateTime update
) {
}
