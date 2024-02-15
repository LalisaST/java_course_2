package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger();
    private final static String NAME = "/list";
    private final static String DESCRIPTION = "Show a list of tracked links";
    private final static String MESSAGE = "List of tracked links: ";

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
        LOGGER.info("Output of a list of tracked links");
        return new SendMessage(update.message().chat().id(), MESSAGE);
    }
}
