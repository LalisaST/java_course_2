package edu.java.scheduler.linkhandlers;

import edu.java.model.Link;
import java.net.URI;

public interface LinkHandler {
    HandlerResult updateLink(Link link);

    boolean supports(URI url);
}
