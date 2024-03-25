package edu.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperTgChatWebClient;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartCommandTest {
    @Mock
    ScrapperTgChatWebClient scrapperTgChatWebClient;

    StartCommand startCommand = new StartCommand(scrapperTgChatWebClient);

    @Test
    @DisplayName("Проверка функции command")
    public void testCommand() {
        assertEquals("/start", startCommand.command());
    }

    @Test
    @DisplayName("Проверка функции description")
    public void testDescription() {
        assertEquals("Register a user", startCommand.description());
    }

    @Test
    @DisplayName("Проверка функции handle")
    public void testHandle() {
        StartCommand startCommand = new StartCommand(scrapperTgChatWebClient);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.chat()).thenReturn(mock(Chat.class));
        when(update.message()).thenReturn(message);
        when(message.chat().id()).thenReturn(123L);
        String MESSAGE = "Registration was successful";

        SendMessage result = startCommand.handle(update);
        SendMessage sendMessage = new SendMessage(123L, MESSAGE);

        assertThat(result).usingRecursiveComparison().isEqualTo(sendMessage);
    }

    @Test
    @DisplayName("Проверка функции supports")
    public void testSupports() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");

        assertTrue(startCommand.supports(update));
    }
}
