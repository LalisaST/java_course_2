package edu.java.scrapper.services.jooq;

import edu.java.scrapper.repositories.jooq.JooqChatDao;
import edu.java.scrapper.repositories.jooq.JooqChatLinkDao;
import edu.java.scrapper.services.DefaultTgChatService;

public class JooqTgChatService extends DefaultTgChatService {
    public JooqTgChatService(JooqChatDao jooqChatDao, JooqChatLinkDao jooqChatLinkDao) {
        super(jooqChatDao, jooqChatLinkDao);
    }
}
