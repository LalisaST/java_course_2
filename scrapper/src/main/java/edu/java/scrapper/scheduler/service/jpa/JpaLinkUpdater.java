package edu.java.scrapper.scheduler.service.jpa;

import edu.java.scrapper.client.BotWebClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.model.entity.Link;
import edu.java.scrapper.scheduler.linkhandler.HandlerResult;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.scheduler.service.LinkUpdater;
import edu.java.scrapper.services.jpa.JpaLinkService;
import edu.java.scrapper.services.jpa.JpaTgChatService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final JpaLinkService jpaLinkService;
    private final JpaTgChatService jpaTgChatService;
    private final List<LinkHandler> linkHandlers;
    private final BotWebClient botWebClient;

    @Override
    @Transactional
    public void update() {
        Long timeCheck = applicationConfig.scheduler().forceCheckDelay().getSeconds();
        List<Link> links = jpaLinkService.searchForUpdateLinks(timeCheck);

        for (Link link : links) {
            linkHandlers.stream()
                .filter(linkHandler -> linkHandler.supports(link.getUrl()))
                .forEach(linkHandler -> processUpdate(linkHandler, link));
        }
    }

    private void processUpdate(LinkHandler linkHandler, Link link) {
        edu.java.scrapper.model.scheme.Link linkScheme = linkMapper(link);
        HandlerResult result = linkHandler.updateLink(linkScheme);
        Long linkId = link.getId();
        List<Long> chatList = jpaTgChatService.findChatsIdById(linkId);

        if (result.update()) {
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(linkId, link.getUrl(), result.description(), chatList);

            updateData(linkId, result);
            botWebClient.update(linkUpdateRequest);
        }

        jpaLinkService.updateLastCheck(linkId, OffsetDateTime.now());

    }

    private edu.java.scrapper.model.scheme.Link linkMapper(Link link) {
        return new edu.java.scrapper.model.scheme.Link(
            link.getId(),
            link.getUrl(),
            link.getLastUpdate(),
            link.getLastCheck(),
            link.getType(),
            link.getCommitCount(),
            link.getAnswerCount(),
            link.getCommentCount()
        );
    }

    private void updateData(Long linkId, HandlerResult result) {
        jpaLinkService.updateLastUpdate(linkId, result.time());
        jpaLinkService.updateCommitCount(linkId, result.commitCount());
        jpaLinkService.updateAnswerCount(linkId, result.answerCount());
        jpaLinkService.updateCommentCount(linkId, result.commentCount());
    }
}
