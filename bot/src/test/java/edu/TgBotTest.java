package edu;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.TgBot;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.user.DefaultUserMessageProcessor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TgBotTest {

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private DefaultUserMessageProcessor userMessageProcessor;

    @Mock
    private ApplicationConfig applicationConfig;

    @Mock
    private Command command1;

    @Mock
    private Command command2;

    private TgBot tgBot;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        List<Command> commands = new ArrayList<>(List.of(command1, command2));

        when(applicationConfig.telegramToken()).thenReturn("token");

        tgBot = new TgBot(applicationConfig, userMessageProcessor, commands);

        Field botField = TgBot.class.getDeclaredField("bot");
        botField.setAccessible(true);
        botField.set(tgBot, telegramBot);
    }

    @Test
    @DisplayName("Проверка функции process")
    void checkingProcessFunction() {
        List<Update> updates = new ArrayList<>();
        updates.add(new Update());

        SendMessage sendMessage = new SendMessage(123, "Hello");
        when(userMessageProcessor.process(any())).thenReturn(sendMessage);

        tgBot.process(updates);

        verify(telegramBot, times(1)).execute(sendMessage);
    }

    @Test
    @DisplayName("Проверка функции start")
    void checkingStartFunction() {
        when(command1.toApiCommand()).thenReturn(new BotCommand("/list", "Show a list of tracked links"));
        when(command2.toApiCommand()).thenReturn(new BotCommand("/start", "Register a user"));

        tgBot.start();

        verify(telegramBot, times(1)).execute(any());
    }

    @Test
    @DisplayName("Проверка функции shutdown")
    void checkingShutdownFunction() {
        tgBot.close();

        verify(telegramBot, times(1)).shutdown();
    }
}
