package edu.java.dto.stackoverflow;

import java.util.List;

public record StackOverflowComment(
    List<Item> items
) {
    public record Item(
    ) {

    }
}
