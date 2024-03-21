package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperTgChatWebClient;
import edu.java.bot.dto.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ScrapperTgChatWebClient scrapperTgChatWebClient;
    private static final String NAME = "/start";
    private static final String DESCRIPTION = "Register a user";
    private static final String MESSAGE = "Registration was successful";

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

        try {
            scrapperTgChatWebClient.registrationChat(chatId);
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
