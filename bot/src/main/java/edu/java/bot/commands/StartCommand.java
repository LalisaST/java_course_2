package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger();
    private final static String NAME = "/start";
    private final static String DESCRIPTION = "Register a user";
    private final static String MESSAGE = "Registration was successful";

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
        LOGGER.info(chatId + " is registered");
        return new SendMessage(chatId, MESSAGE);
    }
}
