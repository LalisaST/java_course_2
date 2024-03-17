package edu.java.repositories.jooq;

import edu.java.model.Chat;
import edu.java.repositories.interfaces.ChatDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Chat.CHAT;

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
