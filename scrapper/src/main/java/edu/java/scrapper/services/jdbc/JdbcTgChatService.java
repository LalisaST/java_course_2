package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.repositories.jdbc.JdbcChatDao;
import edu.java.scrapper.repositories.jdbc.JdbcChatLinkDao;
import edu.java.scrapper.services.DefaultTgChatService;

public class JdbcTgChatService extends DefaultTgChatService {
    public JdbcTgChatService(JdbcChatDao jdbcChatDao, JdbcChatLinkDao jdbcChatLinkDao) {
        super(jdbcChatDao, jdbcChatLinkDao);
    }
}
