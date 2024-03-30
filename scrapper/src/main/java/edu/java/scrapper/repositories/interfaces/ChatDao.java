package edu.java.scrapper.repositories.interfaces;

import edu.java.scrapper.model.scheme.Chat;
import java.util.List;

public interface ChatDao {
    void add(Long id);

    void remove(Long id);

    List<Chat> findAll();
}
