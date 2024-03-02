package edu.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.ListCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListCommandTest {
    ListCommand listCommand = new ListCommand();

    @Test
    @DisplayName("Проверка функции command")
    public void testCommand() {
        assertEquals("/list", listCommand.command());
    }

    @Test
    @DisplayName("Проверка функции description")
    public void testDescription() {
        assertEquals("Show a list of tracked links", listCommand.description());
    }

    @Test
    @DisplayName("Проверка функции handle")
    public void testHandle() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.chat()).thenReturn(mock(Chat.class));
        when(update.message()).thenReturn(message);
        when(message.chat().id()).thenReturn(123L);

        String MESSAGE = "List of tracked links: ";

        SendMessage result = listCommand.handle(update);
        SendMessage sendMessage = new SendMessage(123L, MESSAGE);

        assertThat(result).usingRecursiveComparison().isEqualTo(sendMessage);
    }

    @Test
    @DisplayName("Проверка функции supports")
    public void testSupports() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");

        assertTrue(listCommand.supports(update));
    }
}
