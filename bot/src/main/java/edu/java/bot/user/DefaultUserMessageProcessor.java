package edu.java.bot.user;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserMessageProcessor implements UserMessageProcessor {
    private final List<Command> commands;

    public DefaultUserMessageProcessor(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        if (update == null) {
            return null;
        }

        long chatId = update.message().chat().id();

        for (Command command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return new SendMessage(chatId, "Unknown command");
    }
}
