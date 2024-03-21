package edu.java.scheduler.service.jpa;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.model.entity.Link;
import edu.java.scheduler.linkhandler.HandlerResult;
import edu.java.scheduler.linkhandler.LinkHandler;
import edu.java.scheduler.service.LinkUpdater;
import edu.java.services.DefaultLinkService;
import edu.java.services.DefaultTgChatService;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final JpaLinkService jpaLinkService;
    private final List<LinkHandler> linkHandlers;
    private final BotWebClient botWebClient;

    @Override
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
        edu.java.model.scheme.Link linkScheme = linkMapper(link);
        HandlerResult result = linkHandler.updateLink(linkScheme);
        Long linkId = link.getId();
        List<Long> chatList = jpaLinkService.findChats_IdById(linkId);

        if (result.update()) {
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(linkId, link.getUrl(), result.description(), chatList);
            jpaLinkService.updateLastUpdate(linkId, result.time());
            jpaLinkService.updateLastCheck(linkId, result.time());
            jpaLinkService.updateCommitCount(linkId, result.commitCount());
            jpaLinkService.updateAnswerCount(linkId, result.answerCount());
            jpaLinkService.updateCommentCount(linkId, result.commentCount());

            botWebClient.update(linkUpdateRequest);
        }

        for (Long id : chatList) {
            jpaLinkService.updateLastCheck(id, result.time());
        }
    }

    private edu.java.model.scheme.Link linkMapper(Link link) {
        return new edu.java.model.scheme.Link(
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
}
