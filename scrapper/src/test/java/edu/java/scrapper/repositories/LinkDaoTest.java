package edu.java.scrapper.repositories;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.model.scheme.Link;
import edu.java.scrapper.model.scheme.Type;
import edu.java.scrapper.repositories.interfaces.ChatDao;
import edu.java.scrapper.repositories.interfaces.ChatLinkDao;
import edu.java.scrapper.repositories.interfaces.LinkDao;
import edu.java.scrapper.repositories.jdbc.JdbcChatDao;
import edu.java.scrapper.repositories.jdbc.JdbcChatLinkDao;
import edu.java.scrapper.repositories.jdbc.JdbcLinkDao;
import edu.java.scrapper.repositories.jooq.JooqChatDao;
import edu.java.scrapper.repositories.jooq.JooqChatLinkDao;
import edu.java.scrapper.repositories.jooq.JooqLinkDao;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
public class LinkDaoTest extends IntegrationTest {
    @Autowired
    private JdbcLinkDao jdbcLinkDao;
    @Autowired
    private JdbcChatLinkDao jdbcChatLinkDao;
    @Autowired
    private JdbcChatDao jdbcChatDao;
    @Autowired
    private JooqLinkDao jooqLinkDao;
    @Autowired
    private JooqChatLinkDao jooqChatLinkDao;
    @Autowired
    private JooqChatDao jooqChatDao;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    public void resetSequence() {
        jdbcClient.sql("alter sequence link_id_seq restart with 1").update();
    }

    Arguments[] provideList() {
        return new Arguments[] {
            Arguments.of(jooqLinkDao, jooqChatDao, jooqChatLinkDao),
            Arguments.of(jdbcLinkDao, jdbcChatDao, jdbcChatLinkDao)
        };
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка получения ссылок")
    void findAllTest(LinkDao linkDao) {
        linkDao.add(URI.create("url"), Type.GITHUB);
        List<Link> list = linkDao.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка получения ссылок требуемым обновление")
    void searchForUpdateLinksTest(LinkDao linkDao) {
        OffsetDateTime time = OffsetDateTime.now();
        jdbcClient.sql("insert into link (id, url, last_update, last_check, type, commit_count, "
                       + "answer_count, comment_count) values "
                       + "(1, 'url1', ?, ?, 'GITHUB', 0, 0, 0),"
                       + "(2, 'url2', ?, ?, 'GITHUB', 0, 0, 0)"
        ).params(time, time.minusMinutes(1), time, time).update();

        List<Link> list = linkDao.searchForUpdateLinks(30L);
        assertThat(list.size()).isEqualTo(1);
        List<Link> list1 = linkDao.searchForUpdateLinks(60L);
        assertThat(list1.size()).isEqualTo(0);
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка добавления ссылки")
    void addTest(LinkDao linkDao) {
        List<Link> list = linkDao.findAll();
        assertThat(list.isEmpty()).isTrue();

        linkDao.add(URI.create("url"), Type.GITHUB);
        list = linkDao.findAll();
        assertThat(list.isEmpty()).isFalse();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка удаления ссылки")
    void removeTest(LinkDao linkDao) {
        linkDao.add(URI.create("url"), Type.GITHUB);
        List<Link> list = linkDao.findAll();
        assertThat(list.isEmpty()).isFalse();

        linkDao.remove(1L);
        list = linkDao.findAll();
        assertThat(list.isEmpty()).isTrue();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка поиска ссылки по url")
    void findByUrlTest(LinkDao linkDao) {
        URI url = URI.create("url");
        linkDao.add(url, Type.GITHUB);
        linkDao.add(URI.create("url1"), Type.GITHUB);
        Link link = linkDao.findByUrl(url).orElse(null);
        assertThat(Objects.requireNonNull(link).url()).isEqualTo(url);
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка поиска ссылок по chatId")
    void findLinksByChatIdTest(LinkDao linkDao, ChatDao chatDao, ChatLinkDao chatLinkDao) {
        linkDao.add(URI.create("url"), Type.GITHUB);
        linkDao.add(URI.create("url1"), Type.GITHUB);

        chatDao.add(1L);
        chatDao.add(2L);

        chatLinkDao.add(1L, 1L);
        chatLinkDao.add(2L, 1L);

        assertThat(linkDao.findLinksByChatId(1L).size()).isEqualTo(2);
        assertThat(linkDao.findLinksByChatId(2L).size()).isEqualTo(0);
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Изменение последнего обновления")
    void updateLastUpdateTest(LinkDao linkDao) {
        linkDao.add(URI.create("url"), Type.GITHUB);
        List<Link> link1 = linkDao.findAll();

        OffsetDateTime time1 = OffsetDateTime.now();
        linkDao.updateLastUpdate(1L, time1);
        List<Link> link2 = linkDao.findAll();

        assertThat(link1.equals(link2)).isFalse();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Изменение последней проверки обновления")
    void updateLastCheckTest(LinkDao linkDao) {
        linkDao.add(URI.create("url"), Type.GITHUB);
        List<Link> link1 = linkDao.findAll();

        OffsetDateTime time1 = OffsetDateTime.now();
        linkDao.updateLastCheck(1L, time1);
        List<Link> link2 = linkDao.findAll();

        assertThat(link1.equals(link2)).isFalse();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка изменения данных")
    void updateDateTest(LinkDao linkDao) {
        linkDao.add(URI.create("url"), Type.GITHUB);
        List<Link> link1 = linkDao.findAll();

        linkDao.updateCommitCount(1L, 1);
        List<Link> link2 = linkDao.findAll();

        assertThat(link1.equals(link2)).isFalse();
    }
}
