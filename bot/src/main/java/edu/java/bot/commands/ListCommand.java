package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.scrapper.LinkResponse;
import edu.java.bot.dto.scrapper.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperLinkWebClient scrapperLinkWebClient;
    private static final String NAME = "/list";
    private static final String DESCRIPTION = "Show a list of tracked links";

    @Override
    public String command() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        ListLinksResponse listLinksResponse;

        try {
            listLinksResponse = scrapperLinkWebClient.getAllLinks(chatId);
        } catch (WebClientRequestException e) {
            return new SendMessage(chatId, REQUEST_ERROR);
        } catch (WebClientResponseException e) {
            ApiErrorResponse error = e.getResponseBodyAs(ApiErrorResponse.class);

            if (error != null) {
                return new SendMessage(chatId, error.description());
            }
            return new SendMessage(chatId, RESPONSE_ERROR);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(listLinksResponse.size()).append(" tracked links: ");

        for (LinkResponse link : listLinksResponse.links()) {
            stringBuilder.append(link.url()).append("\n");
        }
        return new SendMessage(update.message().chat().id(), stringBuilder.toString());
    }
}
