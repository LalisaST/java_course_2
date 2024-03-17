package edu.java.repositories.interfaces;

import edu.java.model.ChatLink;
import java.util.List;

public interface ChatLinkDao {
    void add(Long linkId, Long chatId);

    void remove(Long linkId, Long chatId);

    List<ChatLink> findAll();
}
