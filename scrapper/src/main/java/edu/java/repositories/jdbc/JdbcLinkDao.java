package edu.java.repositories.jdbc;

import edu.java.model.scheme.Link;
import edu.java.model.scheme.Type;
import edu.java.repositories.interfaces.LinkDao;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao implements LinkDao {
    private final static String SELECT =
        "select id, url, last_update, last_check, type, commit_count, answer_count, comment_count";
    private final JdbcClient jdbcClient;

    @Override
    public Link add(URI url, Type type) {
        return jdbcClient.sql("insert into link(url, type) values(?, ?) returning id, url, last_update, last_check, "
                              + "type, commit_count, answer_count, comment_count")
            .params(url.toString(), type.toString())
            .query(Link.class)
            .single();
    }

    @Override
    public void remove(Long linkId) {
        jdbcClient.sql("delete from link where id = ?")
            .param(linkId)
            .update();
    }

    @Override
    public List<Link> findAll() {
        return jdbcClient.sql(SELECT + " from link")
            .query(Link.class)
            .list();
    }

    public List<Link> findLinksByChatId(Long chatId) {
        return jdbcClient.sql("select link.id, link.url, link.last_update, link.last_check, link.type, "
                              + "link.commit_count, link.answer_count, link.comment_count from link join chat_link "
                              + "on link.id = chat_link.link_id where chat_link.chat_id = ?")
            .param(chatId)
            .query(Link.class)
            .list();
    }

    public Optional<Link> findByUrl(URI url) {
        return jdbcClient.sql(SELECT + " from link where url = ?")
            .param(url.toString())
            .query(Link.class)
            .optional();
    }

    public List<Link> searchForUpdateLinks(Long second) {
        return jdbcClient.sql(SELECT + " from link "
                              + "where extract(epoch from(current_timestamp - last_check)) > ?")
            .param(second)
            .query(Link.class)
            .list();
    }

    public void updateLastUpdate(Long linkId, OffsetDateTime time) {
        jdbcClient.sql("update link set last_update = ? where id = ?")
            .params(time, linkId)
            .update();
    }

    public void updateLastCheck(Long linkId, OffsetDateTime time) {
        jdbcClient.sql("update link set last_check = ? where id = ?")
            .params(time, linkId)
            .update();
    }

    public void updateCommitCount(Long linkId, Integer count) {
        jdbcClient.sql("update link set commit_count = ? where id = ?")
            .params(count, linkId)
            .update();
    }

    public void updateAnswerCount(Long linkId, Integer count) {
        jdbcClient.sql("update link set answer_count = ? where id = ?")
            .params(count, linkId)
            .update();
    }

    public void updateCommentCount(Long linkId, Integer count) {
        jdbcClient.sql("update link set comment_count = ? where id = ?")
            .params(count, linkId)
            .update();
    }

    public void updateType(Long linkId, Type type) {
        jdbcClient.sql("update link set type = ? where id = ?")
            .params(type.toString(), linkId)
            .update();
    }
}
