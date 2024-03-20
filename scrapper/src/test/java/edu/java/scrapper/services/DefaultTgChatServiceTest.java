package edu.java.scrapper.services;

import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedRegistrationException;
import edu.java.model.Chat;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.services.DefaultTgChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultTgChatServiceTest {
    @Mock
    private JdbcChatDao jdbcChatDao;
    @Mock
    private JdbcChatLinkDao jdbcChatLinkDao;

    private DefaultTgChatService defaultTgChatService;
    private final OffsetDateTime time = OffsetDateTime.now();
    private final Long id = 0L;
    private final Chat chat = new Chat(id, time);

    @BeforeEach
    public void setUp() {
        defaultTgChatService = new DefaultTgChatService(jdbcChatDao, jdbcChatLinkDao);
    }

    @Test
    @DisplayName("Проверка регистрации чата")
    public void checkingChatRegistration() {
        when(jdbcChatDao.findAll()).thenReturn(List.of());
        assertDoesNotThrow(() ->
            defaultTgChatService.registerChat(id));
    }

    @Test
    @DisplayName("Проверка повторной регистрации чата")
    public void checkingRepeatedRegistrationChat() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));

        assertThatThrownBy(() -> defaultTgChatService.registerChat(id)).isInstanceOf(RepeatedRegistrationException.class);
    }

    @Test
    @DisplayName("Проверка удаления чата")
    public void checkingChatDeletion() {
        when(jdbcChatDao.findAll()).thenReturn(List.of(chat));

        assertDoesNotThrow(() -> defaultTgChatService.deleteChat(id));
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    public void deletingNonexistentChat() {
        when(jdbcChatDao.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> defaultTgChatService.deleteChat(id)).isInstanceOf(NotFoundException.class);
    }
}
