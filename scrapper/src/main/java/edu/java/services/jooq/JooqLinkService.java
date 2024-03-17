package edu.java.services.jooq;

import edu.java.repositories.jooq.JooqChatDao;
import edu.java.repositories.jooq.JooqChatLinkDao;
import edu.java.repositories.jooq.JooqLinkDao;
import edu.java.services.DefaultLinkService;
import org.springframework.stereotype.Service;

@Service
public class JooqLinkService extends DefaultLinkService {
    public JooqLinkService(JooqLinkDao jooqLinkDao, JooqChatDao jooqChatDao, JooqChatLinkDao jooqChatLinkDao) {
        super(jooqLinkDao, jooqChatDao, jooqChatLinkDao);
    }
}
