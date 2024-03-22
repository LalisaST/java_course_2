package edu.java.scheduler.service;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.model.scheme.Link;
import edu.java.scheduler.linkhandler.HandlerResult;
import edu.java.scheduler.linkhandler.LinkHandler;
import edu.java.services.DefaultLinkService;
import edu.java.services.DefaultTgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultLinkUpdater implements LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final DefaultLinkService defaultLinkService;
    private final DefaultTgChatService defaultTgChatService;
    private final List<LinkHandler> linkHandlers;
    private final BotWebClient botWebClient;

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

            botWebClient.update(linkUpdateRequest);
        }

        for (Long id : chatList) {
            defaultLinkService.updateLastCheck(id, result.time());
        }
    }

    private void updateData(Long linkId, HandlerResult result) {
        defaultLinkService.updateLastUpdate(linkId, result.time());
        defaultLinkService.updateLastCheck(linkId, result.time());
        defaultLinkService.updateCommitCount(linkId, result.commitCount());
        defaultLinkService.updateAnswerCount(linkId, result.answerCount());
        defaultLinkService.updateCommentCount(linkId, result.commentCount());
    }
}
