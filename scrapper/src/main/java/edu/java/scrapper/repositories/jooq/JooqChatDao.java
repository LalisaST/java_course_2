package edu.java.scrapper.repositories.jooq;

import edu.java.scrapper.model.scheme.Chat;
import edu.java.scrapper.repositories.interfaces.ChatDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatDao implements ChatDao {
    private final DSLContext dslContext;

    @Override
    public void add(Long id) {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, id)
            .execute();
    }

    @Override
    public void remove(Long id) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();
    }

    @Override
    public List<Chat> findAll() {
        return dslContext.selectFrom(CHAT)
            .fetchInto(Chat.class);
    }
}
