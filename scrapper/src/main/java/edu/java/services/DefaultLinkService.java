package edu.java.services;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedLinkException;
import edu.java.model.Chat;
import edu.java.model.Link;
import edu.java.model.Type;
import edu.java.repositories.interfaces.ChatDao;
import edu.java.repositories.interfaces.ChatLinkDao;
import edu.java.repositories.interfaces.LinkDao;
import edu.java.services.interfaces.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultLinkService implements LinkService {
    private final LinkDao linkDao;
    private final ChatDao chatDao;
    private final ChatLinkDao chatLinkDao;

    @Override
    public ListLinksResponse getLinks(Long chatId) {
        searchChat(chatId);

        List<Link> linkList = linkDao.findLinksByChatId(chatId);

        List<LinkResponse> links = linkList.stream()
            .map(this::linkMapper)
            .toList();
        return new ListLinksResponse(links, links.size());
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        URI url = addLinkRequest.url();
        searchChat(chatId);

        int lastIndex = url.getHost().lastIndexOf('.');
        String hostName = url.getHost().substring(0, lastIndex).toUpperCase();

        Link link = linkDao.findByUrl(url)
            .orElseGet(() -> linkDao.add(url, Type.valueOf(hostName)));

        chatLinkDao.findById(link.id(), chatId).ifPresent(e -> {
            throw new RepeatedLinkException("The link is already being tracked");
        });

        chatLinkDao.add(link.id(), chatId);

        return linkMapper(link);
    }

    @Override
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        searchChat(chatId);

        Link link = linkDao.findByUrl(removeLinkRequest.url())
            .orElseThrow(() -> new NotFoundException("The link does not exist"));

        chatLinkDao.findById(link.id(), chatId)
            .orElseThrow(() -> new NotFoundException("The link does not tracked"));

        chatLinkDao.remove(link.id(), chatId);
        chatLinkDao.removeUntraceableLinks();
        return linkMapper(link);
    }

    public List<Link> searchForUpdateLinks(Long second) {
        return linkDao.searchForUpdateLinks(second);
    }

    private void searchChat(Long chatId) {
        List<Chat> chatList = chatDao.findAll();
        if (chatList.stream().noneMatch(chat -> Objects.equals(chat.id(), chatId))) {
            throw new NotFoundException("The chat was not found");
        }
    }

    private LinkResponse linkMapper(Link link) {
        return new LinkResponse(link.id(), link.url());
    }

    public void updateLastUpdate(Long id, OffsetDateTime time) {
        linkDao.updateLastUpdate(id, time);
    }

    public void updateLastCheck(Long id, OffsetDateTime time) {
        linkDao.updateLastCheck(id, time);
    }

    public void updateCommitCount(Long id, Integer count) {
        linkDao.updateCommitCount(id, count);
    }

    public void updateAnswerCount(Long id, Integer count) {
        linkDao.updateAnswerCount(id, count);
    }

    public void updateCommentCount(Long id, Integer count) {
        linkDao.updateCommentCount(id, count);
    }

    public void updateType(Long id, Type type) {
        linkDao.updateType(id, type);
    }
}
