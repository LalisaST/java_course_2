package edu.java.repositories.jpa;

import edu.java.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
