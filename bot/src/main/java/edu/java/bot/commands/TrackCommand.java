package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.link.LinkValidator;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger();
    private final static String NAME = "/track";
    private final static String DESCRIPTION = "Start tracking links";
    private final static String MESSAGE = "The link has been added to the list of tracked";
    private final static String INCORRECT_LINK = "Incorrect link syntax";
    private final static String UNSUITABLE_HOST = "The transferred site is not supported";
    private final static String INCORRECT_COMMAND = "Add a link";

    private final LinkValidator linkValidator;

    public TrackCommand(LinkValidator linkValidator) {
        this.linkValidator = linkValidator;
    }

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
            LOGGER.info(INCORRECT_LINK);
            return new SendMessage(chatId, INCORRECT_LINK);
        }

        if (!linkValidator.isValid(url)) {
            LOGGER.info(UNSUITABLE_HOST);
            return new SendMessage(chatId, UNSUITABLE_HOST);
        }

        LOGGER.info(MESSAGE);
        //TODO: add link
        return new SendMessage(chatId, MESSAGE);
    }
}
