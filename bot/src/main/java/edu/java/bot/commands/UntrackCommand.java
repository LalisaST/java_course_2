package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    private static final String NAME = "/untrack";
    private static final String DESCRIPTION = "Stop tracking the link";
    private static final String MESSAGE = "The link has been removed from the list of tracked";
    private static final String INCORRECT_COMMAND = "Add a link";

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

        //TODO: remove link
        return new SendMessage(chatId, MESSAGE);
    }
}
