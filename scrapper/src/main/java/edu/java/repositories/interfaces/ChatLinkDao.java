package edu.java.repositories.interfaces;

import edu.java.model.scheme.ChatLink;
import java.util.List;
import java.util.Optional;

public interface ChatLinkDao {
    void add(Long linkId, Long chatId);

    void remove(Long linkId, Long chatId);

    List<ChatLink> findAll();

    void removeUntraceableLinks();

    List<Long> findChatIdByLinkId(Long linkId);

    Optional<ChatLink> findById(Long linkId, Long chatId);
}
