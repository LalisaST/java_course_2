package edu.java.scrapper.scheduler.service;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.model.scheme.Link;
import edu.java.scrapper.scheduler.linkhandler.HandlerResult;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.services.DefaultLinkService;
import edu.java.scrapper.services.DefaultTgChatService;
import edu.java.scrapper.services.interfaces.NotificationService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultLinkUpdater implements LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final DefaultLinkService defaultLinkService;
    private final DefaultTgChatService defaultTgChatService;
    private final List<LinkHandler> linkHandlers;
    private final NotificationService service;

    @Override
    public void update() {
        Long timeCheck = applicationConfig.scheduler().forceCheckDelay().getSeconds();
        List<Link> links = defaultLinkService.searchForUpdateLinks(timeCheck);

        for (Link link : links) {
            linkHandlers.stream()
                .filter(linkHandler -> linkHandler.supports(link.url()))
                .forEach(linkHandler -> processUpdate(linkHandler, link));
        }
    }

    private void processUpdate(LinkHandler linkHandler, Link link) {
        HandlerResult result = linkHandler.updateLink(link);
        Long linkId = link.id();
        List<Long> chatList = defaultTgChatService.findChatIdByLinkId(linkId);

        if (result.update()) {
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(linkId, link.url(), result.description(), chatList);
            updateData(linkId, result);

            service.send(linkUpdateRequest);
        }

        defaultLinkService.updateLastCheck(linkId, OffsetDateTime.now());
    }

    private void updateData(Long linkId, HandlerResult result) {
        defaultLinkService.updateLastUpdate(linkId, result.time());
        defaultLinkService.updateCommitCount(linkId, result.commitCount());
        defaultLinkService.updateAnswerCount(linkId, result.answerCount());
        defaultLinkService.updateCommentCount(linkId, result.commentCount());
    }
}
