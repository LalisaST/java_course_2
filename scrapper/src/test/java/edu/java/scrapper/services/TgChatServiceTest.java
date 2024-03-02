package edu.java.scrapper.services;

import edu.java.exeption.NotFoundException;
import edu.java.exeption.RepeatedRegistrationException;
import edu.java.services.TgChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TgChatServiceTest {
    private TgChatService tgChatService;

    @BeforeEach
    public void setUp() {
        tgChatService = new TgChatService();
    }

    @Test
    @DisplayName("Проверка регистрации чата")
    public void checkingChatRegistration() {
        assertDoesNotThrow(() ->
            tgChatService.registerChat(1L));
    }

    @Test
    @DisplayName("Проверка повторной регистрации чата")
    public void checkingRepeatedRegistrationChat() {
        tgChatService.registerChat(1L);

        assertThatThrownBy(() -> tgChatService.registerChat(1L)).isInstanceOf(RepeatedRegistrationException.class);
    }

    @Test
    @DisplayName("Проверка удаления чата")
    public void checkingChatDeletion() {
        tgChatService.registerChat(1L);

        assertDoesNotThrow(() -> tgChatService.deleteChat(1L));
        assertThatThrownBy(() -> tgChatService.deleteChat(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    public void deletingNonexistentChat() {
        tgChatService.registerChat(1L);

        tgChatService.deleteChat(1L);
        assertThatThrownBy(() -> tgChatService.deleteChat(1L)).isInstanceOf(NotFoundException.class);
    }
}
