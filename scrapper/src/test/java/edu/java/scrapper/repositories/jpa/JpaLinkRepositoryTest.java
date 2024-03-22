package edu.java.scrapper.repositories.jpa;

import edu.java.model.entity.Link;
import edu.java.model.scheme.Type;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.IntegrationTest;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class JpaLinkRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcClient jdbcClient;
    @Autowired
    JpaLinkRepository jpaLinkRepository;

    @BeforeEach
    public void setUp() {
        jdbcClient.sql("alter sequence link_id_seq restart with 1").update();
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Проверка получения ссылок")
    void findAll() {
        Link link = new Link();
        link.setUrl(URI.create("uri"));
        link.setId(1L);
        link.setType(Type.STACKOVERFLOW);

        jpaLinkRepository.save(link);

        List<Link> list = jpaLinkRepository.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Проверка поиска ссылки по url")
    void checkingLinkSearchByUrl() {
        jdbcClient.sql(
                "insert into link (id, url, type) values (1, 'url', 'STACKOVERFLOW')")
            .update();

        Link link = jpaLinkRepository.findLinkByUrl(URI.create("url")).get();
        assertThat(link.getUrl()).isEqualTo(URI.create("url"));
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Проверка поиска сылок по chatsId")
    void findAllByChatId() {
        jdbcClient.sql("insert into link (id, url, type) values (1, 'url', 'STACKOVERFLOW')").update();
        jdbcClient.sql("insert into chat (id) values (1)").update();
        jdbcClient.sql("insert into chat_link (link_id, chat_id) values (1, 1)").update();

        List<Link> links = jpaLinkRepository.findAllByChatsId(1L);
        assertThat(links.size()).isEqualTo(1);
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Проверка поиска по chatsId и linkId")
    void findByIdAndChatsId() {
        jdbcClient.sql("insert into link (id, url, type) values (1, 'url', 'STACKOVERFLOW')").update();
        jdbcClient.sql("insert into chat (id) values (1)").update();
        jdbcClient.sql("insert into chat_link (link_id, chat_id) values (1, 1)").update();

        Optional<Link> optionalLink = jpaLinkRepository.findByIdAndChatsId(1L, 1L);
        assertThat(optionalLink).isPresent();
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Проверка получения ссылок требуемых обновление")
    void findByLastCheckGreaterThanSomeSeconds() {
        OffsetDateTime time = OffsetDateTime.now();
        jdbcClient.sql(
                "insert into link (id, url, last_check, type) values "
                + "(1, 'url1', ?, 'STACKOVERFLOW'), "
                + "(2, 'url2', ?, 'STACKOVERFLOW')")
            .params(time, time.minusMinutes(1L))
            .update();

        List<Link> chats = jpaLinkRepository.findByLastCheckGreaterThanSomeSeconds(30L);
        assertThat(chats.size()).isEqualTo(1);
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Изменение последнего обновления")
    void updateLastUpdateById() {
        OffsetDateTime time = OffsetDateTime.now().minusMinutes(15L);
        jdbcClient.sql(
                "insert into link (id, url, last_update, last_check, type) values (1, 'url', ?, ?, 'STACKOVERFLOW')")
            .params(time, time)
            .update();
        jpaLinkRepository.updateLastUpdateById(1L, OffsetDateTime.now());
        Link link = jpaLinkRepository.findById(1L).get();

        assertThat(link.getLastUpdate().isAfter(time)).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Изменение последней проверки обновления")
    void updateLastCheckById() {
        OffsetDateTime time = OffsetDateTime.now().minusMinutes(15L);
        jdbcClient.sql(
                "insert into link (id, url, last_update, last_check, type) values (1, 'url', ?, ?, 'STACKOVERFLOW')")
            .params(time, time)
            .update();
        jpaLinkRepository.updateLastCheckById(1L, OffsetDateTime.now());
        Link link = jpaLinkRepository.findById(1L).get();

        assertThat(link.getLastCheck().isAfter(time)).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("Изменение изменения данных")
    void updateCommitCountById() {
        jdbcClient.sql(
                "insert into link (id, url, type, commit_count) values (1, 'url', 'STACKOVERFLOW', 1)")
            .update();
        jpaLinkRepository.updateCommitCountById(1L, 2);
        Link link = jpaLinkRepository.findById(1L).get();

        assertThat(link.getCommitCount()).isEqualTo(2);
    }
}
