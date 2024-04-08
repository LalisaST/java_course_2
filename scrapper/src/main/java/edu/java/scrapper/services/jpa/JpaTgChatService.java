package edu.java.scrapper.services.jpa;

import edu.java.scrapper.exception.NotFoundException;
import edu.java.scrapper.exception.RepeatedRegistrationException;
import edu.java.scrapper.model.entity.Chat;
import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.services.interfaces.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    public void registerChat(Long chatId) {
        if (jpaChatRepository.existsById(chatId)) {
            throw new RepeatedRegistrationException("The chat is already registered");
        }

        Chat chat = new Chat();
        chat.setId(chatId);
        jpaChatRepository.save(chat);
    }

    @Override
    public void deleteChat(Long chatId) {
        if (jpaChatRepository.existsById(chatId)) {
            jpaChatRepository.deleteById(chatId);
            jpaLinkRepository.deleteAllByChatsEmpty();
        } else {
            throw new NotFoundException("The chat does not exist");
        }
    }

    public List<Long> findChatsIdById(Long linkId) {
        return jpaChatRepository.findByLinksId(linkId).stream()
            .map(Chat::getId)
            .toList();
    }
}
