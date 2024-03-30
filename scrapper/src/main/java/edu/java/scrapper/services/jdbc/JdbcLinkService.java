package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.repositories.jdbc.JdbcChatDao;
import edu.java.scrapper.repositories.jdbc.JdbcChatLinkDao;
import edu.java.scrapper.repositories.jdbc.JdbcLinkDao;
import edu.java.scrapper.services.DefaultLinkService;

public class JdbcLinkService extends DefaultLinkService {
    public JdbcLinkService(JdbcLinkDao jdbcLinkDao, JdbcChatDao jdbcChatDao, JdbcChatLinkDao jdbcChatLinkDao) {
        super(jdbcLinkDao, jdbcChatDao, jdbcChatLinkDao);
    }
}
