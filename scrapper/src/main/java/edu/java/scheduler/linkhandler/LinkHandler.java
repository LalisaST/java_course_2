package edu.java.scheduler.linkhandler;

import edu.java.model.scheme.Link;
import java.net.URI;

public interface LinkHandler {
    HandlerResult updateLink(Link link);

    boolean supports(URI url);
}
