package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.user.DefaultUserMessageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TgBot implements Bot {
    private final static Logger LOGGER = LogManager.getLogger();
    private final TelegramBot bot;
    private final DefaultUserMessageProcessor userMessageProcessor;
    private final List<Command> commands;

    public TgBot(
        ApplicationConfig applicationConfig,
        DefaultUserMessageProcessor userMessageProcessor,
        List<Command> commands
    ) {
        this.bot = new TelegramBot(applicationConfig.telegramToken());
        this.userMessageProcessor = userMessageProcessor;
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        if (updates != null) {
            for (Update update : updates) {
                SendMessage sendMessage = userMessageProcessor.process(update);
                execute(sendMessage);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        creatingListCommands(commands);

        bot.setUpdatesListener(this, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                LOGGER.error(e.getMessage());
            }
        });
    }

    @Override
    @PreDestroy
    public void close() {
        bot.shutdown();
    }

    public void creatingListCommands(List<Command> commands) {
        BotCommand[] botCommands = commands.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);

        this.execute(new SetMyCommands(botCommands));
    }

}
