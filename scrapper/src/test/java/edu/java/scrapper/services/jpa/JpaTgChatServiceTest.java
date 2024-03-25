package edu.java.scrapper.services.jpa;

import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedRegistrationException;
import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.services.jpa.JpaTgChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaTgChatServiceTest {
    @Mock
    private JpaLinkRepository jpaLinkRepository;
    @Mock
    private JpaChatRepository jpaChatRepository;

    private JpaTgChatService jpaTgChatService;
    private final Long id = 0L;

    @BeforeEach
    public void setUp() {
        jpaTgChatService = new JpaTgChatService(jpaChatRepository, jpaLinkRepository);
    }

    @Test
    @DisplayName("Проверка регистрации чата")
    public void checkingChatRegistration() {
        when(jpaChatRepository.existsById(id)).thenReturn(false);
        assertDoesNotThrow(() ->
            jpaTgChatService.registerChat(id));
    }

    @Test
    @DisplayName("Проверка повторной регистрации чата")
    public void checkingRepeatedRegistrationChat() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);

        assertThatThrownBy(() -> jpaTgChatService.registerChat(id)).isInstanceOf(RepeatedRegistrationException.class);
    }

    @Test
    @DisplayName("Проверка удаления чата")
    public void checkingChatDeletion() {
        when(jpaChatRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> jpaTgChatService.deleteChat(id));
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    public void deletingNonexistentChat() {
        when(jpaChatRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> jpaTgChatService.deleteChat(id)).isInstanceOf(NotFoundException.class);
    }
}
