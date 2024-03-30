package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelpCommandTest {
    HelpCommand helpCommand = new HelpCommand();

    @Test
    @DisplayName("Проверка функции command")
    public void testCommand() {
        assertEquals("/help", helpCommand.command());
    }

    @Test
    @DisplayName("Проверка функции description")
    public void testDescription() {
        assertEquals("Display a window with commands", helpCommand.description());
    }

    @Test
    @DisplayName("Проверка функции handle")
    public void testHandle() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.chat()).thenReturn(mock(Chat.class));
        when(update.message()).thenReturn(message);
        when(message.chat().id()).thenReturn(123L);

        String MESSAGE = """
            /start - register a user
            /help - display a window with commands
            /track "link" - start tracking links
            /untrack "link" - stop tracking the link
            /list - show a list of tracked links""";

        SendMessage result = helpCommand.handle(update);
        SendMessage sendMessage = new SendMessage(123L, MESSAGE);

        assertThat(result).usingRecursiveComparison().isEqualTo(sendMessage);
    }

    @Test
    @DisplayName("Проверка функции supports")
    public void testSupports() {

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/help");

        assertTrue(helpCommand.supports(update));
    }
}
