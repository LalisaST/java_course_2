package edu.java.scrapper.repositories.jpa;

import edu.java.scrapper.model.entity.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByLinksId(Long linkId);
}
