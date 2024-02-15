package edu.java.bot.link;

import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LinkValidator {
    List<Link> listLinks;

    public LinkValidator(List<Link> listLinks) {
        this.listLinks = listLinks;
    }

    public boolean isValid(URI url) {
        for (Link link : listLinks) {
            if (url.getHost().equals(link.getHostName()) && !url.getPath().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
