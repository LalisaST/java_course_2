package edu.java.repositories.jooq;

import edu.java.model.Link;
import edu.java.model.Type;
import edu.java.repositories.interfaces.LinkDao;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.ChatLink.CHAT_LINK;
import static edu.java.domain.jooq.tables.Link.LINK;

@Repository
@RequiredArgsConstructor
public class JooqLinkDao implements LinkDao {
    private final DSLContext dslContext;

    @Override
    public Link add(URI url, Type type) {
        return dslContext.insertInto(LINK)
            .set(LINK.URL, url.toString())
            .set(LINK.TYPE, type.toString())
            .returningResult()
            .fetchSingleInto(Link.class);
    }

    @Override
    public void remove(Long linkId) {
        dslContext.deleteFrom(LINK)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    @Override
    public List<Link> findAll() {
        return dslContext.selectFrom(LINK)
            .fetchInto(Link.class);
    }

    public List<Link> findLinksByChatId(Long chatId) {
        return dslContext.select(LINK)
            .from(LINK.join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))).where(CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    public Optional<Link> findByUrl(URI url) {
        return dslContext.selectFrom(LINK)
            .where(LINK.URL.eq(url.toString()))
            .fetchOptionalInto(Link.class);
    }

    public List<Link> searchForUpdateLinks(Long second) {
        return dslContext.selectFrom(LINK)
            .where("extract(epoch from(current_timestamp - {0})) > {1}", LINK.LAST_CHECK, second)
            .fetchInto(Link.class);
    }

    public void updateLastUpdate(Long linkId, OffsetDateTime time) {
        dslContext.update(LINK)
            .set(LINK.LAST_UPDATE, time)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    public void updateLastCheck(Long linkId, OffsetDateTime time) {
        dslContext.update(LINK)
            .set(LINK.LAST_CHECK, time)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    public void updateCommitCount(Long linkId, Integer count) {
        dslContext.update(LINK)
            .set(LINK.COMMIT_COUNT, count)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    public void updateAnswerCount(Long linkId, Integer count) {
        dslContext.update(LINK)
            .set(LINK.ANSWER_COUNT, count)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    public void updateCommentCount(Long linkId, Integer count) {
        dslContext.update(LINK)
            .set(LINK.COMMENT_COUNT, count)
            .where(LINK.ID.eq(linkId))
            .execute();
    }

    public void updateType(Long linkId, Type type) {
        dslContext.update(LINK)
            .set(LINK.TYPE, type.toString())
            .execute();
    }
}
