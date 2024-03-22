package edu.java.services.jdbc;

import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.services.DefaultTgChatService;

public class JdbcTgChatService extends DefaultTgChatService {
    public JdbcTgChatService(JdbcChatDao jdbcChatDao, JdbcChatLinkDao jdbcChatLinkDao) {
        super(jdbcChatDao, jdbcChatLinkDao);
    }
}
