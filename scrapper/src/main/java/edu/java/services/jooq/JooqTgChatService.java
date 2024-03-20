package edu.java.services.jooq;

import edu.java.repositories.jooq.JooqChatDao;
import edu.java.repositories.jooq.JooqChatLinkDao;
import edu.java.services.DefaultTgChatService;
import org.springframework.stereotype.Service;

@Service
public class JooqTgChatService extends DefaultTgChatService {
    public JooqTgChatService(JooqChatDao jooqChatDao, JooqChatLinkDao jooqChatLinkDao) {
        super(jooqChatDao, jooqChatLinkDao);
    }
}
