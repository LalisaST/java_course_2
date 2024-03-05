package edu.java.services;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedLinkException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private final Map<Long, List<LinkResponse>> chatLinks = new HashMap<>();
    private Long idLink = 0L;

    public ListLinksResponse getLinks(Long id) {
        searchChat(id);

        List<LinkResponse> links = chatLinks.get(id);
        return new ListLinksResponse(links, links.size());
    }

    public LinkResponse addLink(Long id, AddLinkRequest addLinkRequest) {
        searchChat(id);

        List<LinkResponse> links = chatLinks.get(id);

        if (links.stream().anyMatch(link -> link.url().equals(addLinkRequest.url()))) {
            throw new RepeatedLinkException("The link is already being tracked");
        }

        LinkResponse linkResponse = new LinkResponse(idLink++, addLinkRequest.url());
        links.add(linkResponse);
        chatLinks.put(id, links);
        return linkResponse;
    }

    public LinkResponse deleteLink(Long id, RemoveLinkRequest removeLinkRequest) {
        searchChat(id);

        LinkResponse linkResponse = chatLinks.get(id).stream()
            .filter(linkR -> linkR.url().equals(removeLinkRequest.url()))
            .findAny()
            .orElseThrow(() -> new NotFoundException("The link does not exist"));

        chatLinks.get(id).remove(linkResponse);
        return linkResponse;
    }

    private void searchChat(Long id) {
        if (!chatLinks.containsKey(id)) {
            throw new NotFoundException("The chat was not found");
        }
    }

    //Метод для тестов, в будущем удалю, когда репозиторий добавится
    public void addChat(Long id, List<LinkResponse> linkResponseList) {
        chatLinks.put(id, linkResponseList);
    }
}
