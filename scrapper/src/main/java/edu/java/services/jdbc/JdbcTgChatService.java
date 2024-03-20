package edu.java.services.jdbc;

import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.services.DefaultTgChatService;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgChatService extends DefaultTgChatService {
    public JdbcTgChatService(JdbcChatDao jdbcChatDao, JdbcChatLinkDao jdbcChatLinkDao) {
        super(jdbcChatDao, jdbcChatLinkDao);
    }
}
