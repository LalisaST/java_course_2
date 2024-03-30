package edu.java.scrapper.scheduler.linkhandler;

import edu.java.scrapper.model.scheme.Link;
import java.net.URI;

public interface LinkHandler {
    HandlerResult updateLink(Link link);

    boolean supports(URI url);
}
