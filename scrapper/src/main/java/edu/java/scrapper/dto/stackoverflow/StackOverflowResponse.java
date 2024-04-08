package edu.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponse(
    List<Item> items
) {
    public record Item(
        String link,

        @JsonAlias("last_activity_date")
        OffsetDateTime lastActivity
    ) {

    }
}
