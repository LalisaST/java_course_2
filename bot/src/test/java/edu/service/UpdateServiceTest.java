package edu.service;

import edu.java.bot.Bot;
import edu.java.bot.dto.bot.LinkUpdateRequest;
import edu.java.bot.service.UpdateService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateServiceTest {
    @Mock
    private Bot bot;

    @Test
    @DisplayName("Проверка отправки обновления")
    public void checkingSendingUpdate() {
        LinkUpdateRequest linkUpdateRequest =
            new LinkUpdateRequest(1L, URI.create("url"), "description", List.of(1L, 2L, 3L));

        UpdateService updateService = new UpdateService(bot);
        updateService.sendUpdate(linkUpdateRequest);

        verify(bot, times(3)).execute(any());
    }
}
