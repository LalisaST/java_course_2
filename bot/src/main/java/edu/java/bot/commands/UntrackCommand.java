package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.scrapper.RemoveLinkRequest;
import edu.java.bot.link.LinkValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperLinkWebClient scrapperLinkWebClient;
    private static final String NAME = "/untrack";
    private static final String DESCRIPTION = "Stop tracking the link";
    private static final String INCORRECT_LINK = "Incorrect link syntax";
    private static final String UNSUITABLE_HOST = "The transferred site is not supported";
    private static final String MESSAGE = "The link has been removed from the list of tracked";
    private static final String INCORRECT_COMMAND = "Incorrect command";
    private final LinkValidator linkValidator;

    @Override
    public String command() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public boolean supports(Update update) {
        String[] text = update.message().text().strip().split(" ");
        return text[0].equals(command());
    }

    @Override
    public SendMessage handle(Update update) {
        String[] text = update.message().text().strip().split(" ");
        long chatId = update.message().chat().id();

        if (text.length != 2) {
            return new SendMessage(chatId, INCORRECT_COMMAND);
        }

        URI url;
        try {
            url = URI.create(text[1]);
        } catch (IllegalAccessError e) {
            return new SendMessage(chatId, INCORRECT_LINK);
        }

        if (!linkValidator.isValid(url)) {
            return new SendMessage(chatId, UNSUITABLE_HOST);
        }

        try {
            scrapperLinkWebClient.deleteLink(chatId, new RemoveLinkRequest(url));
        } catch (WebClientRequestException e) {
            return new SendMessage(chatId, REQUEST_ERROR);
        } catch (WebClientResponseException e) {
            ApiErrorResponse error = e.getResponseBodyAs(ApiErrorResponse.class);

            if(error != null) {
                return new SendMessage(chatId, error.description());
            }
            return new SendMessage(chatId, RESPONSE_ERROR);
        }
        return new SendMessage(chatId, MESSAGE);
    }
}
