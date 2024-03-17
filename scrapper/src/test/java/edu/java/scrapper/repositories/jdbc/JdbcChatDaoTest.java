package edu.java.scrapper.repositories.jdbc;

import edu.java.model.Chat;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JdbcChatDaoTest extends IntegrationTest {
    @Autowired
    private JdbcChatDao jdbcChatDao;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка получения чатов")
    void findAllTest() {
        jdbcChatDao.add(1L);
        List<Chat> list = jdbcChatDao.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка добавления чата")
    void addTest() {
        List<Chat> list = jdbcChatDao.findAll();
        assertThat(list.isEmpty()).isTrue();

        jdbcChatDao.add(1L);
        list = jdbcChatDao.findAll();
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка удаления чата")
    void removeTest() {
        Long id = 1L;
        jdbcChatDao.add(id);
        List<Chat> list = jdbcChatDao.findAll();
        assertThat(list.isEmpty()).isFalse();

        jdbcChatDao.remove(id);
        list = jdbcChatDao.findAll();
        assertThat(list.isEmpty()).isTrue();
    }
}
