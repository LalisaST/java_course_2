package edu.java.dto.stackoverflow;

import java.util.List;

public record StackOverflowAnswer(
    List<Item> items
) {
    public record Item(
    ) {

    }
}
