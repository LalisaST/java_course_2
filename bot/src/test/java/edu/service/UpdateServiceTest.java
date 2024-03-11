package edu.service;

import edu.java.bot.dto.bot.LinkUpdateRequest;
import edu.java.bot.exception.UpdateAlreadyExistsException;
import edu.java.bot.service.UpdateService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UpdateServiceTest {
    private UpdateService updateService;
    private LinkUpdateRequest linkUpdateRequest;

    @BeforeEach
    public void setUp() {
        updateService = new UpdateService();
        linkUpdateRequest = new LinkUpdateRequest(1L, URI.create("url"), "description", List.of(1L, 2L, 3L));
    }

    @Test
    @DisplayName("Проверка добавления обновления")
    public void checkingAddingUpdate() {

        assertDoesNotThrow(() -> updateService.addUpdate(linkUpdateRequest));
    }

    @Test
    @DisplayName("Проверка повторного добавления обновления")
    public void checkingRepeatedUpdate() {

        LinkUpdateRequest linkUpdateRequest2 =
            new LinkUpdateRequest(1L, URI.create("url"), "description", List.of(1L, 2L, 3L));

        updateService.addUpdate(linkUpdateRequest);

        assertThatThrownBy(() -> updateService.addUpdate(linkUpdateRequest2)).isInstanceOf(UpdateAlreadyExistsException.class);
    }
}
