package edu.java.scrapper.repositories.jdbc;

import edu.java.model.ChatLink;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.repositories.jdbc.JdbcLinkDao;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcChatLinkDaoTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkDao jdbcChatLinkDao;
    @Autowired
    private JdbcChatDao jdbcChatDao;
    @Autowired
    private JdbcLinkDao jdbcLinkDao;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    public void resetSequence() {
        jdbcClient.sql("alter sequence link_id_seq restart with 1").update();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка получения данных")
    void findAllTest() {
        jdbcChatDao.add(1L);
        jdbcLinkDao.add(URI.create("url"));
        jdbcChatLinkDao.add(1L, 1L);

        List<ChatLink> list = jdbcChatLinkDao.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка добавления данных")
    void addTest() {
        List<ChatLink> list = jdbcChatLinkDao.findAll();
        assertThat(list.isEmpty()).isTrue();

        jdbcChatDao.add(1L);
        jdbcLinkDao.add(URI.create("url"));
        jdbcChatLinkDao.add(1L, 1L);

        list = jdbcChatLinkDao.findAll();
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка удаления данных")
    void removeTest() {
        jdbcChatDao.add(1L);
        jdbcLinkDao.add(URI.create("url"));
        jdbcChatLinkDao.add(1L, 1L);

        List<ChatLink> list = jdbcChatLinkDao.findAll();
        assertThat(list.isEmpty()).isFalse();

        jdbcChatLinkDao.remove(1L, 1L);
        list = jdbcChatLinkDao.findAll();
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка удаления неотслеживаемых ссылок")
    void removeUntraceableLinksTest() {
        jdbcChatDao.add(1L);
        jdbcLinkDao.add(URI.create("url"));
        jdbcChatLinkDao.add(1L, 1L);

        assertThat(jdbcLinkDao.findAll().size()).isEqualTo(1);

        jdbcChatDao.remove(1L);
        jdbcChatLinkDao.removeUntraceableLinks();

        assertThat(jdbcLinkDao.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка поиска чатов по linkId")
    void findChatIdByLinkIdTest() {
        jdbcChatDao.add(1L);
        jdbcChatDao.add(2L);
        jdbcLinkDao.add(URI.create("url"));
        jdbcChatLinkDao.add(1L, 1L);
        jdbcChatLinkDao.add(1L, 2L);

        assertThat(jdbcChatLinkDao.findChatIdByLinkId(1L)).isEqualTo(List.of(1L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка поиска данных по id")
    void findById() {
        jdbcChatDao.add(1L);
        jdbcChatDao.add(2L);
        jdbcLinkDao.add(URI.create("url"));
        jdbcChatLinkDao.add(1L, 1L);
        jdbcChatLinkDao.add(1L, 2L);

        assertThat(jdbcChatLinkDao.findById(1L, 1L))
            .isEqualTo(Optional.of(new ChatLink(1L, 1L)));
    }
}
