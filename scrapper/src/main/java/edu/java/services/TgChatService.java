package edu.java.services;

import edu.java.exeption.NotFoundException;
import edu.java.exeption.RepeatedRegistrationException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class TgChatService {
    private final Set<Long> chats = new HashSet<>();

    public void registerChat(Long id) {
        if (!chats.add(id)) {
            throw new RepeatedRegistrationException("The chat is already registered");
        }
    }

    public void deleteChat(Long id) {
        if (!chats.remove(id)) {
            throw new NotFoundException("The chat does not exist");
        }
    }
}
