package edu.java.scheduler.linkupdaters;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.model.Link;
import edu.java.scheduler.LinkUpdater;
import edu.java.scheduler.linkhandlers.HandlerResult;
import edu.java.scheduler.linkhandlers.LinkHandler;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private final ApplicationConfig applicationConfig;
    private final JdbcLinkService jdbcLinkService;
    private final JdbcTgChatService jdbcTgChatService;
    private final List<LinkHandler> linkHandlers;
    private final BotWebClient botWebClient;

    @Override
    public void update() {
        Long timeCheck = applicationConfig.scheduler().forceCheckDelay().getSeconds();
        List<Link> links = jdbcLinkService.searchForUpdateLinks(timeCheck);

        for (Link link : links) {
            linkHandlers.stream()
                .filter(linkHandler -> linkHandler.supports(link.url()))
                .forEach(linkHandler -> processUpdate(linkHandler, link));
        }
    }

    private void processUpdate(LinkHandler linkHandler, Link link) {
        HandlerResult result = linkHandler.updateLink(link);
        List<Long> chatList = jdbcTgChatService.findChatIdByLinkId(link.id());

        if (result.update()) {
            LinkUpdateRequest linkUpdateRequest =
                new LinkUpdateRequest(link.id(), link.url(), result.description(), chatList);

            botWebClient.update(linkUpdateRequest);

            for (Long id : chatList) {
                jdbcLinkService.updateLastUpdate(id, result.time());
            }
        }

        for (Long id : chatList) {
            jdbcLinkService.updateLastCheck(id, result.time());
        }
    }
}
