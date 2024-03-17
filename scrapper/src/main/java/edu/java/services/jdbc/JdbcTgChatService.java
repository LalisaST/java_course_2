package edu.java.services.jdbc;

import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedRegistrationException;
import edu.java.model.Chat;
import edu.java.repositories.jdbc.JdbcChatDao;
import edu.java.repositories.jdbc.JdbcChatLinkDao;
import edu.java.services.interfaces.TgChatService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatDao jdbcChatDao;
    private final JdbcChatLinkDao jdbcChatLinkDao;

    @Override
    public void registerChat(Long chatId) {
        if (searchChat(chatId)) {
            throw new RepeatedRegistrationException("The chat is already registered");
        }
        jdbcChatDao.add(chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        if (searchChat(chatId)) {
            jdbcChatDao.remove(chatId);
            jdbcChatLinkDao.removeUntraceableLinks();
        } else {
            throw new NotFoundException("The chat does not exist");
        }
    }

    public  List<Long> findChatIdByLinkId(Long linkId) {
        return jdbcChatLinkDao.findChatIdByLinkId(linkId);
    }

    private boolean searchChat(Long chatId) {
        List<Chat> chatList = jdbcChatDao.findAll();
        return chatList.stream().anyMatch(chat -> Objects.equals(chat.id(), chatId));
    }
}
