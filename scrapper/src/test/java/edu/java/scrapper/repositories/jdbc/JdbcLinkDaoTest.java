package edu.java.scrapper.repositories.jdbc;

import edu.java.model.Link;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.repositories.jdbc.JdbcLinkDao;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JdbcLinkDaoTest extends IntegrationTest {
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
    @DisplayName("Проверка получения ссылок")
    void findAllTest() {
        jdbcLinkDao.add(URI.create("url"));
        List<Link> list = jdbcLinkDao.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка получения ссылок требуемым обновление")
    void searchForUpdateLinksTest() {
        OffsetDateTime time = OffsetDateTime.now();
        jdbcClient.sql("insert into link (id, url, last_update, last_check) values "
                       + "(1, 'url1', ?, ?),"
                       + "(2, 'url2', ?, ?)"
        ).params(time, time.minusMinutes(1), time, time).update();

        List<Link> list = jdbcLinkDao.searchForUpdateLinks(30L);
        assertThat(list.size()).isEqualTo(1);
        List<Link> list1 = jdbcLinkDao.searchForUpdateLinks(60L);
        assertThat(list1.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка добавления ссылки")
    void addTest() {
        List<Link> list = jdbcLinkDao.findAll();
        assertThat(list.isEmpty()).isTrue();

        jdbcLinkDao.add(URI.create("url"));
        list = jdbcLinkDao.findAll();
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка удаления ссылки")
    void removeTest() {
        jdbcLinkDao.add(URI.create("url"));
        List<Link> list = jdbcLinkDao.findAll();
        assertThat(list.isEmpty()).isFalse();

        jdbcLinkDao.remove(1L);
        list = jdbcLinkDao.findAll();
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка поиска ссылки по url")
    void findByUrlTest() {
        URI url = URI.create("url");
        jdbcLinkDao.add(url);
        jdbcLinkDao.add(URI.create("url1"));
        Link link = jdbcLinkDao.findByUrl(url).orElse(null);
        assertThat(Objects.requireNonNull(link).url()).isEqualTo(url);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Проверка поиска ссылок по chatId")
    void findLinksByChatIdTest() {
        jdbcLinkDao.add(URI.create("url"));
        jdbcLinkDao.add(URI.create("url1"));

        JdbcChatDao jdbcChatDao = new JdbcChatDao(jdbcClient);
        jdbcChatDao.add(1L);
        jdbcChatDao.add(2L);

        JdbcChatLinkDao jdbcChatLinkDao = new JdbcChatLinkDao(jdbcClient);
        jdbcChatLinkDao.add(1L, 1L);
        jdbcChatLinkDao.add(2L, 1L);

        assertThat(jdbcLinkDao.findLinksByChatId(1L).size()).isEqualTo(2);
        assertThat(jdbcLinkDao.findLinksByChatId(2L).size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Изменение последнего обновления")
    void updateLastUpdateTest() {
        jdbcLinkDao.add(URI.create("url"));
        List<Link> link1 = jdbcLinkDao.findAll();

        OffsetDateTime time1 = OffsetDateTime.now();
        jdbcLinkDao.updateLastUpdate(1L, time1);
        List<Link> link2 = jdbcLinkDao.findAll();

        assertThat(link1.equals(link2)).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Изменение последней проверки обновления")
    void updateLastCheckTest() {
        jdbcLinkDao.add(URI.create("url"));
        List<Link> link1 = jdbcLinkDao.findAll();

        OffsetDateTime time1 = OffsetDateTime.now();
        jdbcLinkDao.updateLastCheck(1L, time1);
        List<Link> link2 = jdbcLinkDao.findAll();

        assertThat(link1.equals(link2)).isFalse();
    }
}
