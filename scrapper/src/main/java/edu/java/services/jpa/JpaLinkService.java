package edu.java.services.jpa;

import edu.java.dto.scrapper.AddLinkRequest;
import edu.java.dto.scrapper.LinkResponse;
import edu.java.dto.scrapper.ListLinksResponse;
import edu.java.dto.scrapper.RemoveLinkRequest;
import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedLinkException;
import edu.java.model.entity.Chat;
import edu.java.model.entity.Link;
import edu.java.model.scheme.Type;
import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.services.interfaces.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private static final String EXCEPTION_CHAT_NOT_FOUND = "The chat was not found";
    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    public ListLinksResponse getLinks(Long chatId) {
        searchChat(chatId);
        List<Link> linkList = jpaLinkRepository.findAllByChatsId(chatId);

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

        Link newLink = new Link();
        newLink.setUrl(url);
        newLink.setType(Type.valueOf(hostName));

        Link link = jpaLinkRepository.findLinkByUrl(url)
            .orElseGet(() -> jpaLinkRepository.save(newLink));

        jpaLinkRepository.findByIdAndChatsId(link.getId(), chatId).ifPresent(e -> {
            throw new RepeatedLinkException("The link is already being tracked");
        });

        Chat chat =
            jpaChatRepository.findById(chatId).orElseThrow(() -> new NotFoundException(EXCEPTION_CHAT_NOT_FOUND));
        chat.addLink(link);
        jpaChatRepository.save(chat);

        return linkMapper(link);
    }

    @Override
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        URI url = removeLinkRequest.url();
        searchChat(chatId);

        Link link = jpaLinkRepository.findLinkByUrl(url)
            .orElseThrow(() -> new NotFoundException("The link does not exist"));

        jpaLinkRepository.findByIdAndChatsId(link.getId(), chatId)
            .orElseThrow(() -> new NotFoundException("The link does not tracked"));

        Chat chat =
            jpaChatRepository.findById(chatId).orElseThrow(() -> new NotFoundException(EXCEPTION_CHAT_NOT_FOUND));
        chat.removeLink(link);
        jpaLinkRepository.deleteAllByChatsEmpty();

        return linkMapper(link);
    }

    private void searchChat(Long chatId) {
        if (!jpaChatRepository.existsById(chatId)) {
            throw new NotFoundException(EXCEPTION_CHAT_NOT_FOUND);
        }
    }

    public List<Link> searchForUpdateLinks(Long second) {
        return jpaLinkRepository.findByLastCheckGreaterThanSomeSeconds(second);
    }

    private LinkResponse linkMapper(Link link) {
        return new LinkResponse(link.getId(), link.getUrl());
    }

    public void updateLastUpdate(Long id, OffsetDateTime time) {
        jpaLinkRepository.updateLastUpdateById(id, time);
    }

    public void updateLastCheck(Long id, OffsetDateTime time) {
        jpaLinkRepository.updateLastCheckById(id, time);
    }

    public void updateCommitCount(Long id, Integer count) {
        jpaLinkRepository.updateCommitCountById(id, count);
    }

    public void updateAnswerCount(Long id, Integer count) {
        jpaLinkRepository.updateAnswerCountById(id, count);
    }

    public void updateCommentCount(Long id, Integer count) {
        jpaLinkRepository.updateCommentCountById(id, count);
    }

    public void updateType(Long id, Type type) {
        jpaLinkRepository.updateTypeById(id, type);
    }
}
