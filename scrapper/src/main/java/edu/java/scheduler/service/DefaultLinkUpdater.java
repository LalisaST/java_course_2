package edu.java.scheduler.service;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.model.Link;
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
        List<Long> chatList = defaultTgChatService.findChatIdByLinkId(link.id());

        if (result.update()) {
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(link.id(), link.url(), result.description(), chatList);

            botWebClient.update(linkUpdateRequest);

            for (Long id : chatList) {
                defaultLinkService.updateLastUpdate(id, result.time());
                defaultLinkService.updateCommentCount(id, result.commentCount());
                defaultLinkService.updateAnswerCount(id, result.answerCount());
                defaultLinkService.updateCommitCount(id, result.commitCount());
            }
        }

        for (Long id : chatList) {
            defaultLinkService.updateLastCheck(id, result.time());
        }
    }
}
