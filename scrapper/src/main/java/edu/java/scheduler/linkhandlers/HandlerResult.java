package edu.java.scheduler.linkhandlers;

import java.time.OffsetDateTime;

public record HandlerResult(boolean update, String description, OffsetDateTime time) {
}
