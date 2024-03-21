package edu.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.link.LinkValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UntrackCommandTest {
    @Mock
    ScrapperLinkWebClient scrapperLinkWebClient;
    LinkValidator linkValidator;
    UntrackCommand untrackCommand = new UntrackCommand(scrapperLinkWebClient, linkValidator);

    @Test
    @DisplayName("Проверка функции command")
    public void testCommand() {
        assertEquals("/untrack", untrackCommand.command());
    }

    @Test
    @DisplayName("Проверка функции description")
    public void testDescription() {
        assertEquals("Stop tracking the link", untrackCommand.description());
    }

    @Test
    @DisplayName("Проверка функции handle")
    public void testHandle() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.chat()).thenReturn(mock(Chat.class));
        when(message.text()).thenReturn("123");
        when(update.message()).thenReturn(message);
        when(message.chat().id()).thenReturn(123L);

        String MESSAGE = "Incorrect command";

        SendMessage result = untrackCommand.handle(update);
        SendMessage sendMessage = new SendMessage(123L, MESSAGE);

        assertThat(result).usingRecursiveComparison().isEqualTo(sendMessage);
    }

    @Test
    @DisplayName("Проверка функции supports")
    public void testSupports() {

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack");

        assertTrue(untrackCommand.supports(update));
    }
}
