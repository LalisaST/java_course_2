package edu.java.services.jdbc;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedLinkException;
import edu.java.model.Chat;
import edu.java.model.Link;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.repositories.jdbc.JdbcLinkDao;
import edu.java.services.interfaces.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkDao jdbcLinkDao;
    private final JdbcChatDao jdbcChatDao;
    private final JdbcChatLinkDao jdbcChatLinkDao;

    @Override
    public ListLinksResponse getLinks(Long chatId) {
        searchChat(chatId);

        List<Link> linkList = jdbcLinkDao.findLinksByChatId(chatId);

        List<LinkResponse> links = linkList.stream()
            .map(this::linkMapper)
            .toList();
        return new ListLinksResponse(links, links.size());
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        URI url = addLinkRequest.url();
        searchChat(chatId);

        Link link = jdbcLinkDao.findByUrl(url)
            .orElseGet(() -> jdbcLinkDao.add(url));

        jdbcChatLinkDao.findById(link.id(), chatId).ifPresent(e -> {
            throw new RepeatedLinkException("The link is already being tracked");
        });

        jdbcChatLinkDao.add(link.id(), chatId);

        return linkMapper(link);
    }

    @Override
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        searchChat(chatId);

        Link link = jdbcLinkDao.findByUrl(removeLinkRequest.url())
            .orElseThrow(() -> new NotFoundException("The link does not exist"));

        jdbcChatLinkDao.findById(link.id(), chatId)
            .orElseThrow(() -> new NotFoundException("The link does not tracked"));

        jdbcChatLinkDao.remove(link.id(), chatId);
        jdbcChatLinkDao.removeUntraceableLinks();
        return linkMapper(link);
    }

    public List<Link> searchForUpdateLinks(Long second) {
        return jdbcLinkDao.searchForUpdateLinks(second);
    }

    private void searchChat(Long chatId) {
        List<Chat> chatList = jdbcChatDao.findAll();
        if (chatList.stream().noneMatch(chat -> Objects.equals(chat.id(), chatId))) {
            throw new NotFoundException("The chat was not found");
        }
    }

    private LinkResponse linkMapper(Link link) {
        return new LinkResponse(link.id(), link.url());
    }

    public void updateLastUpdate(Long id, OffsetDateTime time) {
        jdbcLinkDao.updateLastUpdate(id, time);
    }

    public void updateLastCheck(Long id, OffsetDateTime time) {
        jdbcLinkDao.updateLastCheck(id, time);
    }
}
