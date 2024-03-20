package edu.java.services.jdbc;

import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.repositories.jdbc.JdbcLinkDao;
import edu.java.services.DefaultLinkService;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService extends DefaultLinkService {
    public JdbcLinkService(JdbcLinkDao jdbcLinkDao, JdbcChatDao jdbcChatDao, JdbcChatLinkDao jdbcChatLinkDao) {
        super(jdbcLinkDao, jdbcChatDao, jdbcChatLinkDao);
    }
}
