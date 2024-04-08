package edu.java.scrapper.services.interfaces;

import edu.java.scrapper.dto.scrapper.AddLinkRequest;
import edu.java.scrapper.dto.scrapper.LinkResponse;
import edu.java.scrapper.dto.scrapper.ListLinksResponse;
import edu.java.scrapper.dto.scrapper.RemoveLinkRequest;

public interface LinkService {
    ListLinksResponse getLinks(Long chatId);

    LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest);
}
