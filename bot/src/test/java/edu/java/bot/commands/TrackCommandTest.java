package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.link.GitHubLink;
import edu.java.bot.link.Link;
import edu.java.bot.link.LinkValidator;
import edu.java.bot.link.StackOverflowLink;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
public class TrackCommandTest {
    @Mock
    ScrapperLinkWebClient scrapperLinkWebClient;
    LinkValidator linkValidator;
    TrackCommand trackCommand;

    @BeforeEach
    public void setup() {
        List<Link> listLinks = Arrays.asList(new StackOverflowLink(), new GitHubLink());
        linkValidator = new LinkValidator(listLinks);
        trackCommand = new TrackCommand(scrapperLinkWebClient, linkValidator);
    }

    @Test
    @DisplayName("Проверка функции command")
    public void testCommand() {
        assertEquals("/track", trackCommand.command());
    }

    @Test
    @DisplayName("Проверка функции description")
    public void testDescription() {
        assertEquals("Start tracking links", trackCommand.description());
    }

    @Test
    @DisplayName("Проверка функции handle")
    public void testHandle() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(message.chat()).thenReturn(mock(Chat.class));
        when(message.text()).thenReturn("/track https://github.com/LalisaST?tab=repositories");
        when(update.message()).thenReturn(message);
        when(message.chat().id()).thenReturn(123L);

        String MESSAGE = "The link has been added to the list of tracked";

        SendMessage result = trackCommand.handle(update);
        SendMessage sendMessage = new SendMessage(123L, MESSAGE);

        assertThat(result).usingRecursiveComparison().isEqualTo(sendMessage);
    }

    @Test
    @DisplayName("Проверка функции supports")
    public void testSupports() {

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track");

        assertTrue(trackCommand.supports(update));
    }
}
