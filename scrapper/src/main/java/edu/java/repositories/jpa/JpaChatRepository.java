package edu.java.repositories.jpa;

import edu.java.model.entity.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByLinksId(Long linkId);
}
