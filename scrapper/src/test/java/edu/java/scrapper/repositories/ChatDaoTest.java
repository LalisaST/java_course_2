package edu.java.scrapper.repositories;

import edu.java.model.scheme.Chat;
import edu.java.repositories.interfaces.ChatDao;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jooq.JooqChatDao;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
public class ChatDaoTest extends IntegrationTest {
    @Autowired
    private JdbcChatDao jdbcChatDao;
    @Autowired
    private JooqChatDao jooqChatDao;

    Arguments[] provideList() {
        return new Arguments[] {
            Arguments.of(jdbcChatDao),
            Arguments.of(jooqChatDao)
        };
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка получения чатов")
    void findAllTest(ChatDao chatDao) {
        chatDao.add(1L);
        List<Chat> list = chatDao.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка добавления чата")
    void addTest(ChatDao chatDao) {
        List<Chat> list = chatDao.findAll();
        assertThat(list.isEmpty()).isTrue();

        chatDao.add(1L);
        list = chatDao.findAll();
        assertThat(list.isEmpty()).isFalse();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка удаления чата")
    void removeTest(ChatDao chatDao) {
        Long id = 1L;
        chatDao.add(id);
        List<Chat> list = chatDao.findAll();
        assertThat(list.isEmpty()).isFalse();

        chatDao.remove(id);
        list = chatDao.findAll();
        assertThat(list.isEmpty()).isTrue();
    }
}
