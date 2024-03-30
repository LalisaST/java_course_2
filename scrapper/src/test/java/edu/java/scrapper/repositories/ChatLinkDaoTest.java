package edu.java.scrapper.repositories;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.model.scheme.ChatLink;
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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatLinkDaoTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkDao jdbcChatLinkDao;
    @Autowired
    private JdbcChatDao jdbcChatDao;
    @Autowired
    private JdbcLinkDao jdbcLinkDao;

    @Autowired
    private JooqLinkDao jooqLinkDao;
    @Autowired
    private JooqChatDao jooqChatDao;
    @Autowired
    private JooqChatLinkDao jooqChatLinkDao;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    public void resetSequence() {
        jdbcClient.sql("alter sequence link_id_seq restart with 1").update();
    }

    Arguments[] provideList() {
        return new Arguments[] {
            Arguments.of(jdbcChatDao, jdbcLinkDao, jdbcChatLinkDao),
            Arguments.of(jooqChatDao, jooqLinkDao, jooqChatLinkDao)
        };
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка получения данных")
    void findAllTest(ChatDao chatDao, LinkDao linkDao, ChatLinkDao chatLinkDao) {
        chatDao.add(1L);
        linkDao.add(URI.create("url"), Type.GITHUB);
        chatLinkDao.add(1L, 1L);

        List<ChatLink> list = chatLinkDao.findAll();
        assertThat(list.size() == 1).isTrue();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка добавления данных")
    void addTest(ChatDao chatDao, LinkDao linkDao, ChatLinkDao chatLinkDao) {
        List<ChatLink> list = chatLinkDao.findAll();
        assertThat(list.isEmpty()).isTrue();

        chatDao.add(1L);
        linkDao.add(URI.create("url"), Type.GITHUB);
        chatLinkDao.add(1L, 1L);

        list = chatLinkDao.findAll();
        assertThat(list.isEmpty()).isFalse();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка удаления данных")
    void removeTest(ChatDao chatDao, LinkDao linkDao, ChatLinkDao chatLinkDao) {
        chatDao.add(1L);
        linkDao.add(URI.create("url"), Type.GITHUB);
        chatLinkDao.add(1L, 1L);

        List<ChatLink> list = chatLinkDao.findAll();
        assertThat(list.isEmpty()).isFalse();

        chatLinkDao.remove(1L, 1L);
        list = chatLinkDao.findAll();
        assertThat(list.isEmpty()).isTrue();
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка удаления неотслеживаемых ссылок")
    void removeUntraceableLinksTest(ChatDao chatDao, LinkDao linkDao, ChatLinkDao chatLinkDao) {
        chatDao.add(1L);
        linkDao.add(URI.create("url"), Type.GITHUB);
        chatLinkDao.add(1L, 1L);

        assertThat(linkDao.findAll().size()).isEqualTo(1);

        chatDao.remove(1L);
        chatLinkDao.removeUntraceableLinks();

        assertThat(linkDao.findAll().size()).isEqualTo(0);
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка поиска чатов по linkId")
    void findChatIdByLinkIdTest(ChatDao chatDao, LinkDao linkDao, ChatLinkDao chatLinkDao) {
        chatDao.add(1L);
        chatDao.add(2L);
        linkDao.add(URI.create("url"), Type.GITHUB);
        chatLinkDao.add(1L, 1L);
        chatLinkDao.add(1L, 2L);

        assertThat(chatLinkDao.findChatIdByLinkId(1L)).isEqualTo(List.of(1L, 2L));
    }

    @Transactional
    @Rollback
    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Проверка поиска данных по id")
    void findById(ChatDao chatDao, LinkDao linkDao, ChatLinkDao chatLinkDao) {
        chatDao.add(1L);
        chatDao.add(2L);
        linkDao.add(URI.create("url"), Type.GITHUB);
        chatLinkDao.add(1L, 1L);
        chatLinkDao.add(1L, 2L);

        assertThat(chatLinkDao.findById(1L, 1L))
            .isEqualTo(Optional.of(new ChatLink(1L, 1L)));
    }
}
