package edu.java.repositories.jdbc;

import edu.java.model.scheme.ChatLink;
import edu.java.repositories.interfaces.ChatLinkDao;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkDao implements ChatLinkDao {
    private final JdbcClient jdbcClient;

    @Override
    public void add(Long linkId, Long chatId) {
        jdbcClient.sql("insert into chat_link(link_id, chat_id) values(?, ?)")
            .params(linkId, chatId)
            .update();
    }

    @Override
    public void remove(Long linkId, Long chatId) {
        jdbcClient.sql("delete from chat_link where link_id = ? and chat_id = ?")
            .params(linkId, chatId)
            .update();
    }

    @Override
    public List<ChatLink> findAll() {
        return jdbcClient.sql("select link_id, chat_id from chat_link").query(ChatLink.class).list();
    }

    public void removeUntraceableLinks() {
        jdbcClient.sql(
                "delete from link where not exists(select 1 from chat_link where chat_link.link_id = link.id)")
            .update();
    }

    public List<Long> findChatIdByLinkId(Long linkId) {
        return jdbcClient.sql("select chat_id from chat_link where link_id = ?")
            .param(linkId)
            .query(Long.class)
            .list();
    }

    public Optional<ChatLink> findById(Long linkId, Long chatId) {
        return jdbcClient.sql(
                "select link_id, chat_id from chat_link where link_id = ? and chat_id = ?")
            .params(linkId, chatId)
            .query(ChatLink.class).optional();
    }
}
