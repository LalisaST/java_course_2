package edu.java.repositories.interfaces;

import edu.java.model.scheme.Chat;
import java.util.List;

public interface ChatDao {
    void add(Long id);

    void remove(Long id);

    List<Chat> findAll();
}
