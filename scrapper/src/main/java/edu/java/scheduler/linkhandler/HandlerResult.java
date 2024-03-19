package edu.java.scheduler.linkhandler;

import java.time.OffsetDateTime;

public record HandlerResult(boolean update, String description, OffsetDateTime time) {
}
