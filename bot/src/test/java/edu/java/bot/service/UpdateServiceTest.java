package edu.java.bot.service;

import edu.java.bot.Bot;
import edu.java.bot.dto.bot.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import io.micrometer.core.instrument.Counter;
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
    @Mock
    private Counter counter;

    @Test
    @DisplayName("Проверка отправки обновления")
    public void checkingSendingUpdate() {
        LinkUpdateRequest linkUpdateRequest =
            new LinkUpdateRequest(1L, URI.create("url"), "description", List.of(1L, 2L, 3L));

        UpdateService updateService = new UpdateService(bot, counter);
        updateService.sendUpdate(linkUpdateRequest);

        verify(bot, times(3)).execute(any());
    }
}
