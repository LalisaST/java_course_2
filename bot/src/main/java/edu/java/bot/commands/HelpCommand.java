package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private static final String NAME = "/help";
    private static final String DESCRIPTION = "Display a window with commands";
    private static final String MESSAGE = """
            /start - register a user
            /help - display a window with commands
            /track "link" - start tracking links
            /untrack "link" - stop tracking the link
            /list - show a list of tracked links""";

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
        return new SendMessage(update.message().chat().id(), MESSAGE);
    }
}
