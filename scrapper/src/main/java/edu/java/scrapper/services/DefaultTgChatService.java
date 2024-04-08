package edu.java.scrapper.services;

import edu.java.scrapper.exception.NotFoundException;
import edu.java.scrapper.exception.RepeatedRegistrationException;
import edu.java.scrapper.model.scheme.Chat;
import edu.java.scrapper.repositories.interfaces.ChatDao;
import edu.java.scrapper.repositories.interfaces.ChatLinkDao;
import edu.java.scrapper.services.interfaces.TgChatService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultTgChatService implements TgChatService {
    private final ChatDao chatDao;
    private final ChatLinkDao chatLinkDao;

    @Override
    public void registerChat(Long chatId) {
        if (searchChat(chatId)) {
            throw new RepeatedRegistrationException("The chat is already registered");
        }
        chatDao.add(chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        if (searchChat(chatId)) {
            chatDao.remove(chatId);
            chatLinkDao.removeUntraceableLinks();
        } else {
            throw new NotFoundException("The chat does not exist");
        }
    }

    public List<Long> findChatIdByLinkId(Long linkId) {
        return chatLinkDao.findChatIdByLinkId(linkId);
    }

    private boolean searchChat(Long chatId) {
        List<Chat> chatList = chatDao.findAll();
        return chatList.stream().anyMatch(chat -> Objects.equals(chat.id(), chatId));
    }
}
