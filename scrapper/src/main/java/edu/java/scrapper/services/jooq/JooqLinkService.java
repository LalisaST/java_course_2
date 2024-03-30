package edu.java.scrapper.services.jooq;

import edu.java.scrapper.repositories.jooq.JooqChatDao;
import edu.java.scrapper.repositories.jooq.JooqChatLinkDao;
import edu.java.scrapper.repositories.jooq.JooqLinkDao;
import edu.java.scrapper.services.DefaultLinkService;

public class JooqLinkService extends DefaultLinkService {
    public JooqLinkService(JooqLinkDao jooqLinkDao, JooqChatDao jooqChatDao, JooqChatLinkDao jooqChatLinkDao) {
        super(jooqLinkDao, jooqChatDao, jooqChatLinkDao);
    }
}
