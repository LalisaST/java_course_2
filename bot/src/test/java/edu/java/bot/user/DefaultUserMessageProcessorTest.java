package edu.java.bot.user;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultUserMessageProcessorTest {
    @Mock
    private Command command;

    private DefaultUserMessageProcessor defaultUserMessageProcessor;

    @BeforeEach
    void setUp() {
        List<Command> commands = new ArrayList<>(List.of(command));
        defaultUserMessageProcessor = new DefaultUserMessageProcessor(commands);
    }

    @Test
    @DisplayName("Проверка функции process")
    void checkingProcessFunction() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.chat()).thenReturn(mock(Chat.class));
        when(update.message()).thenReturn(message);
        when(message.chat().id()).thenReturn(123L);

        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Unknown command");

        when(command.supports(update)).thenReturn(false);

        SendMessage result = defaultUserMessageProcessor.process(update);

        assertThat(result).usingRecursiveComparison().isEqualTo(sendMessage);

    }

    @Test
    @DisplayName("Проверка ввода null")
    void checkingNullInput() {
        SendMessage result = defaultUserMessageProcessor.process(null);
        assertNull(result);
    }
}
