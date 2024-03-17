package edu.java.repositories.jooq;

import edu.java.model.ChatLink;
import edu.java.repositories.interfaces.ChatLinkDao;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;

@Repository
@RequiredArgsConstructor
public class JooqChatLinkDao implements ChatLinkDao {
    private final DSLContext dslContext;

    @Override
    public void add(Long linkId, Long chatId) {
        dslContext.insertInto(CHAT_LINK)
            .set(CHAT_LINK.LINK_ID, linkId)
            .set(CHAT_LINK.CHAT_ID, chatId)
            .execute();
    }

    @Override
    public void remove(Long linkId, Long chatId) {
        dslContext.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(linkId)
                .and(CHAT_LINK.CHAT_ID.eq(chatId)))
            .execute();
    }

    @Override
    public List<ChatLink> findAll() {
        return dslContext.selectFrom(CHAT_LINK)
            .fetchInto(ChatLink.class);
    }

    public void removeUntraceableLinks() {
        dslContext.deleteFrom(LINK)
            .whereNotExists(
                dslContext.selectOne()
                    .from(CHAT_LINK)
                    .where(CHAT_LINK.LINK_ID.eq(LINK.ID)))
            .execute();
    }

    public List<Long> findChatIdByLinkId(Long linkId) {
        return dslContext.select(CHAT_LINK.CHAT_ID)
            .from(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(Long.class);
    }

    public Optional<ChatLink> findById(Long linkId, Long chatId) {
        return dslContext.selectFrom(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(linkId)
                .and(CHAT_LINK.CHAT_ID.eq(chatId)))
            .fetchOptionalInto(ChatLink.class);
    }
}
