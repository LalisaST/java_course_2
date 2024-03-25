package edu.java.scrapper.repositories.jpa;

import edu.java.model.entity.Chat;
import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.IntegrationTest;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JpaChatRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcClient jdbcClient;
    @Autowired
    JpaChatRepository jpaChatRepository;

    @Transactional
    @Rollback
    @Test
    @DisplayName("Проверка поиска чатов по linkId")
    public void findChatsIdById() {
        jdbcClient.sql("insert into link (id, url, type) values (1, 'url', 'STACKOVERFLOW')").update();
        jdbcClient.sql("insert into chat (id) values (1)").update();
        jdbcClient.sql("insert into chat_link (link_id, chat_id) values (1, 1)").update();

        List<Chat> chats = jpaChatRepository.findByLinksId(1L);
        assertThat(chats.size()).isEqualTo(1);
    }
}
