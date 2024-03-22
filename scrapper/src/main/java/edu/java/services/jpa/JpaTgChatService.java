package edu.java.services.jpa;

import edu.java.exception.NotFoundException;
import edu.java.exception.RepeatedRegistrationException;
import edu.java.model.entity.Chat;
import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.services.interfaces.TgChatService;
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
}
