package edu.java.repositories.jdbc;

import edu.java.model.scheme.Chat;
import edu.java.repositories.interfaces.ChatDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatDao implements ChatDao {
    private final JdbcClient jdbcClient;

    @Override
    public void add(Long id) {
        jdbcClient.sql("insert into chat(id, create_at) values(?, current_timestamp)")
            .param(id)
            .update();
    }

    @Override
    public void remove(Long id) {
        jdbcClient.sql("delete from chat where id = ?")
            .param(id)
            .update();
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient.sql("select id, create_at from chat").query(Chat.class).list();
    }
}
